package com.yugyd.quiz.core.file

import android.net.Uri
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

interface FileRepository {

    @Throws(IOException::class, FileNotFoundException::class)
    fun saveTextToLocalStorage(fileName: String, fileContents: String): File

    fun checkUri(uri: String): Boolean

    fun getFileName(uri: String): String?

    @Throws(IOException::class)
    fun readTextFromUri(uri: String): String

    @Throws(IOException::class, FileNotFoundException::class)
    fun readTextFromFile(fileName: String): String
}
