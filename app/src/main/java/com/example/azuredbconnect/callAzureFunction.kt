package com.example.azuredbconnect
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

suspend fun callAzureFunction(tableName: String, params: Map<String, String>): String {
    val queryParams =
        params.map { (key, value) -> "$key=${URLEncoder.encode(value, "UTF-8")}" }.joinToString("&")
    val urlString = "https://serverconnect.azurewebsites.net/api/Updatetables"
    val url = URL(urlString)
    val connection = url.openConnection() as HttpURLConnection
    println(queryParams)
    println(url)
    println(connection)
    connection.requestMethod = "POST"
    connection.doOutput = true

    // Add the Azure Function key to the request header
    val azureFunctionKey = "f97R6DS633LqccKGomZAmopKsTVtZqQs3HkCnMLYGtgYAzFuaWDpBw=="
    connection.setRequestProperty("x-functions-key", azureFunctionKey)

    // Write the query parameters to the request body
    OutputStreamWriter(connection.outputStream).use { writer ->
        writer.write(queryParams)
    }

    val responseCode = connection.responseCode
    val responseMessage = connection.responseMessage
    println(responseCode)
    println(responseMessage)

    if (responseCode == HttpURLConnection.HTTP_OK) {
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = reader.readText()
        reader.close()
        return response
    } else {
        val errorStream = connection.errorStream
        val errorReader = BufferedReader(InputStreamReader(errorStream))
        val errorResponse = errorReader.readText()
        errorReader.close()

        throw Exception("Failed to call Azure Function. Response code: $responseCode, Response message: $responseMessage, Error response: $errorResponse")
    }
}