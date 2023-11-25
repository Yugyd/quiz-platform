package com.yugyd.quiz.core.file

import android.net.Uri
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

interface FileRepository {

    @Throws(IOException::class, FileNotFoundException::class)
    fun saveTextToLocalStorage(fileName: String, fileContents: String): File

    fun checkUri(uri: Uri): Boolean

    @Throws(IOException::class)
    fun readTextFromUri(uri: Uri): String

    @Throws(IOException::class, FileNotFoundException::class)
    fun readTextFromFile(fileName: String): String
}
