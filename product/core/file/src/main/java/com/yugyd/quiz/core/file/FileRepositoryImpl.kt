package com.yugyd.quiz.core.file

import android.content.Context
import android.database.Cursor
import android.net.Uri
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader

class FileRepositoryImpl(private val context: Context) : FileRepository {

    override fun saveTextToLocalStorage(fileName: String, fileContents: String): File {
        val file = File(context.filesDir, fileName)

        if (!file.exists()) {
            file.createNewFile()
        }

        file.outputStream().use {
            it.write(fileContents.toByteArray())
        }

        return file
    }

    override fun checkUri(uri: Uri): Boolean {
        val contentResolver = context.contentResolver

        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null, null)
        val result = cursor?.use {
            it.count > 1
        }
        return result ?: false
    }

    override fun readTextFromUri(uri: Uri): String {
        val contentResolver = context.contentResolver

        val stringBuilder = StringBuilder()

        contentResolver.openInputStream(uri)?.use { inputStream ->
            val inputStreamReader = InputStreamReader(inputStream)
            BufferedReader(inputStreamReader).use { reader ->
                var line: String? = reader.readLine()

                while (line != null) {
                    stringBuilder.append(line)
                    line = reader.readLine()
                }
            }
        }

        return stringBuilder.toString()
    }

    override fun readTextFromFile(fileName: String): String {
        val file = File(context.filesDir, fileName)

        if (!file.exists()) {
            throw FileNotFoundException("$fileName not founded")
        }

        val stringBuilder = StringBuilder()

        file.inputStream().use { inputStream ->
            val inputStreamReader = InputStreamReader(inputStream)
            BufferedReader(inputStreamReader).use { reader ->
                var line: String? = reader.readLine()

                while (line != null) {
                    stringBuilder.append(line)
                    line = reader.readLine()
                }
            }
        }

        return stringBuilder.toString()
    }
}
