import {onCall} from "firebase-functions/v2/https";
import {initializeApp} from "firebase-admin/app";
import {getFirestore} from "firebase-admin/firestore";
import {GoogleGenerativeAI} from "@google/generative-ai";
import {defineSecret} from "firebase-functions/params";

const geminiApiKey = defineSecret("GEMINI_API_KEY");

initializeApp();
const db = getFirestore();

const TARGET_LANGUAGES = ["pt", "es"];

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
        // PROMPT MELHORADO: Atua como tradutor nativo de jogos
        const prompt =
          "You are an expert native translator for trivia games. Translate these " +
          `${missingItems.length} questions into the language: "${lang}".\n` +
          "STRICT RULES:\n" +
          "1. Ensure grammar is perfect and the language sounds natural, fluid, and exciting for a game.\n" +
          "2. DO NOT translate proper nouns (people, bands, movies, brands).\n" +
          "3. Maintain the exact original IDs and JSON structure.\n" +
          `Return a JSON object with a key 'results' containing the translated items. Items: ${JSON.stringify(missingItems)}`;

        const result = await model.generateContent({
          contents: [{role: "user", parts: [{text: prompt}]}],
          generationConfig: {responseMimeType: "application/json"},
        });

        const translatedBatch = JSON.parse(result.response.text());

        for (const item of translatedBatch.results as QuizQuestion[]) {
          const itemId = item.id.toString();

          // BLINDAGEM DE CÓDIGO: Remove qualquer nó extra inventado pelo Gemini
          delete item.translations;

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

    const orderedResults = (questions as QuizQuestion[]).map((q) => {
      const found = results.find((r) => r.id.toString() === q.id.toString());
      return found || q;
    });

    return {results: orderedResults};
  }
);
