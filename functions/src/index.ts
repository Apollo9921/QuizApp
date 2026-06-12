import {onCall} from "firebase-functions/v2/https";
import {initializeApp} from "firebase-admin/app";
import {getFirestore} from "firebase-admin/firestore";
import {GoogleGenerativeAI} from "@google/generative-ai";
import {defineSecret} from "firebase-functions/params";

const geminiApiKey = defineSecret("GEMINI_API_KEY");

initializeApp();
const db = getFirestore();

const TARGET_LANGUAGES = ["pt", "es"];

// Interface para remover os avisos de "any" do Linter
interface QuizQuestion {
  id: string | number;
  question: string;
  correctAnswer: string;
  incorrectAnswers: string[];
  translations?: Record<string, QuizQuestion>;
}

export const getTranslatedQuiz = onCall(
  {
    secrets: [geminiApiKey],
    invoker: "public",
  },
  async (request) => {
    const ai = new GoogleGenerativeAI(geminiApiKey.value());
    const model = ai.getGenerativeModel({model: "gemini-2.5-flash"});
    const {questions, language} = request.data;

    const normalizedLanguage = language.toLowerCase();

    if (normalizedLanguage === "en") {
      return {results: questions};
    }

    // Tipagem estrita usando a Interface criada
    const results: QuizQuestion[] = [];
    const missingByLanguage: Record<string, QuizQuestion[]> = {};

    TARGET_LANGUAGES.forEach((lang) => {
      missingByLanguage[lang] = [];
    });

    for (const q of questions as QuizQuestion[]) {
      const docId = q.id.toString();
      const doc = await db.collection("questions").doc(docId).get();
      const docData = doc.data() as QuizQuestion | undefined;
      const translations = docData?.translations || {};

      if (doc.exists && translations[normalizedLanguage]) {
        results.push(translations[normalizedLanguage]);
      }

      TARGET_LANGUAGES.forEach((lang) => {
        if (!translations[lang]) {
          missingByLanguage[lang].push(q);
        }
      });
    }

    for (const lang of TARGET_LANGUAGES) {
      const missingItems = missingByLanguage[lang];

      if (missingItems.length > 0) {
        // Quebra de strings longas para respeitar o limite de 80 caracteres
        const prompt =
          "You are an expert quiz translator. Translate these " +
          `${missingItems.length} quiz questions to the language: ${lang}. ` +
          "Return a JSON object with a key 'results' containing the " +
          "translated items. Maintain original IDs, citation context, and " +
          `internal double quotes. Items: ${JSON.stringify(missingItems)}`;

        const result = await model.generateContent({
          contents: [{role: "user", parts: [{text: prompt}]}],
          generationConfig: {responseMimeType: "application/json"},
        });

        const translatedBatch = JSON.parse(result.response.text());

        for (const item of translatedBatch.results as QuizQuestion[]) {
          const itemId = item.id.toString();

          await db.collection("questions").doc(itemId).set({
            translations: {[lang]: item},
          }, {merge: true});

          if (lang === normalizedLanguage) {
            const alreadyAdded = results.some(
              (r) => r.id.toString() === itemId
            );
            if (!alreadyAdded) {
              results.push(item);
            }
          }
        }
      }
    }

    // Mapeamento final tipado sem estourar os 80 caracteres por linha
    const orderedResults = (questions as QuizQuestion[]).map((q) => {
      const found = results.find((r) => r.id.toString() === q.id.toString());
      return found || q;
    });

    return {results: orderedResults};
  }
);
