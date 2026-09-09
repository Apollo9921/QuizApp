/* eslint-disable */
import * as admin from "firebase-admin";
import {GoogleGenerativeAI} from "@google/generative-ai";
import * as path from "path";

const serviceAccount = require(path.join(__dirname, "../serviceAccountKey.json"));

if (!admin.apps.length) {
  admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
  });
}

const db = admin.firestore();
const GEMINI_API_KEY = process.env.GEMINI_API_KEY || "MY_GEMINI_API_KEY";
const ai = new GoogleGenerativeAI(GEMINI_API_KEY);
const model = ai.getGenerativeModel({model: "gemini-2.5-flash"});

const TARGET_LANGUAGES = ["pt", "es"];
const BATCH_SIZE = 15;

async function refineAndCleanTranslations() {
  console.log("🚀 A iniciar a revisão de texto e limpeza do aninhamento...");

  const snapshot = await db.collection("questions").get();
  const allDocs = snapshot.docs;
  console.log(`📦 Total de documentos: ${allDocs.length}`);

  for (let i = 0; i < allDocs.length; i += BATCH_SIZE) {
    const chunkDocs = allDocs.slice(i, i + BATCH_SIZE);

    for (const lang of TARGET_LANGUAGES) {
      const itemsToImprove = [];

      for (const doc of chunkDocs) {
        const data = doc.data();
        const existingTranslation = data.translations?.[lang];

        if (existingTranslation && existingTranslation.question) {
          itemsToImprove.push({
            id: existingTranslation.id || doc.id,
            question: existingTranslation.question,
            correctAnswer: existingTranslation.correctAnswer,
            incorrectAnswers: existingTranslation.incorrectAnswers,
          });
        }
      }

      if (itemsToImprove.length === 0) continue;

      console.log(`🔄 A melhorar texto do lote ${Math.floor(i / BATCH_SIZE) + 1} para (${lang.toUpperCase()})...`);

      const prompt =
        `Você é um editor nativo especialista em quizzes de trívia. Abaixo estão perguntas no idioma "${lang}". ` +
        "Algumas têm traduções literais, robóticas ou pouco naturais. A sua tarefa é REVER e MELHORAR o texto.\n" +
        "REGRAS OBRIGATÓRIAS:\n" +
        "1. Corrija a gramática e torne a linguagem natural, fluida e empolgante para um jogo.\n" +
        "2. NÃO traduza nomes próprios de pessoas, bandas, filmes ou marcas (ex: \"Let It Be\", \"Shane Warne\").\n" +
        "3. Mantenha o formato exato dos dados.\n" +
        `Retorne um objeto JSON com a chave 'results' contendo os itens melhorados. Itens: ${JSON.stringify(itemsToImprove)}`;

      try {
        const result = await model.generateContent({
          contents: [{role: "user", parts: [{text: prompt}]}],
          generationConfig: {responseMimeType: "application/json"},
        });

        const refinedBatch = JSON.parse(result.response.text());
        const bulkWriter = db.bulkWriter();

        // IMPEDE QUE O SCRIPT CRASHE EM CASO DE ERRO PONTUAL
        bulkWriter.onWriteError((error) => {
          console.error(`⚠️ Erro isolado ignorado no doc ${error.documentRef.path}. O script vai continuar.`);
          return false;
        });

        for (let j = 0; j < refinedBatch.results.length; j++) {
          const item = refinedBatch.results[j];

          // BLINDAGEM DE ID: Cruzamos com o ID que o Gemini devolveu, ou usamos a mesma posição do array original
          const originalItem = itemsToImprove.find((i) => i.id == item.id) || itemsToImprove[j];
          if (!originalItem) continue;

          const safeId = originalItem.id;
          item.id = safeId; // Sobrescreve o ID do Gemini com o ID verdadeiro

          delete item.translations;

          const docRef = db.collection("questions").doc(safeId.toString());
          bulkWriter.update(docRef, {
            [`translations.${lang}`]: item,
          });
        }

        await bulkWriter.close();
        console.log(`✅ Lote atualizado com sucesso em (${lang.toUpperCase()}).`);
      } catch (error) {
        console.error(`❌ Erro de processamento no lote (${lang}) - a avançar para o próximo:`, error);
      }
    }
  }

  console.log("🎉 Concluído! O texto foi melhorado e a estrutura do Firestore está limpa.");
}

refineAndCleanTranslations();
