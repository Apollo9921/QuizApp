package com.apollo9921.quizrise.presentation.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import kotlin.math.ln
import kotlin.math.pow

fun formatTotalCount(count: Float): String {
    if (count < 1000) return count.toInt().toString()
    val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
    return String.format(
        "%.1f %c",
        count / 1000.0.pow(exp.toDouble()),
        "kMGTPE"[exp - 1]
    )
}

fun shareQuizBitmap(context: Context, bitmap: Bitmap, shareText: String, titleIntent: String) {
    val cachePath = File(context.cacheDir, "images").apply { mkdirs() }
    val file = File(cachePath, "quizrise_result.png")

    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    val contentUri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )

    val playStoreLink = "https://play.google.com/store/apps/details?id=${context.packageName}"
    val shareText = "$shareText $playStoreLink"

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, contentUri)
        putExtra(Intent.EXTRA_TEXT, shareText)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(intent, titleIntent))
}