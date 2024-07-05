package com.example.androidgithubsearch

import okhttp3.mockwebserver.MockResponse
import java.io.BufferedReader
import java.io.InputStreamReader

fun MockResponse.setBodyFromFileName(fileName: String): MockResponse {
    val inputStream = javaClass.classLoader.getResourceAsStream(fileName)
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    val stringBuffer = StringBuilder()
    bufferedReader.forEachLine { stringBuffer.append(it) }

    val body = stringBuffer.toString()
    this.setBody(body)
    return this
}