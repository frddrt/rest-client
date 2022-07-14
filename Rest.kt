package br.com.frddrt.helpers

import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class Rest {

    private var code: Int = 0
    private var result: String? = null

    val string: String? get() = result
    val jsonObject: JSONObject get() = result?.let { JSONObject(it) } ?: JSONObject()
    val jsonArray: JSONArray get() = result?.let { JSONArray(it) } ?: JSONArray()
    val responseCode: Int get() = code

    fun get(
        address: String,
        parameters: Map<String, Any>? = null,
        token: String? = null,
        propertys: Map<String, Any>? = null
    ) {
        val builder = StringBuilder()

        if (parameters != null) {
            for ((key, value) in parameters) {
                if (builder.isNotEmpty()) builder.append("&")
                builder
                    .append(URLEncoder.encode(key, "UTF-8"))
                    .append("=")
                    .append(URLEncoder.encode(value.toString(), "UTF-8"))
            }
        }

        if (builder.isNotEmpty()) builder.insert(0, "?")
        builder.insert(0, address)

        val url = URL(builder.toString())
        val connection = url.openConnection() as HttpURLConnection

        result = try {
            connection.requestMethod = "GET"

            if (token != null) connection.addRequestProperty("Authorization", "Bearer $token")
            if (propertys != null) setPropertysToConnection(connection, propertys)

            val inputStream = connection.inputStream
            code = connection.responseCode

            inputStream.bufferedReader().use(BufferedReader::readText)
        } catch (error: Exception) {
            code = -1
            NOT_CONNECTED
        } finally {
            connection.disconnect()
        }
    }

    fun post(
        address: String,
        parameters: Map<String, Any>,
        token: String? = null,
        propertys: Map<String, Any>? = null
    ) {
        val url = URL(address)
        val connection = url.openConnection() as HttpURLConnection

        result = try {
            connection.requestMethod = "POST"

            if (token != null) connection.addRequestProperty("Authorization", "Bearer $token")
            if (propertys != null) setPropertysToConnection(connection, propertys)

            setDataBody(connection, parameters)

            val inputStream = connection.inputStream
            code = connection.responseCode

            inputStream.bufferedReader().use(BufferedReader::readText)
        } catch (error: Exception) {
            code = -1
            NOT_CONNECTED
        } finally {
            connection.disconnect()
        }
    }

    fun put(
        address: String,
        id: String,
        parameters: Map<String, Any>,
        token: String? = null,
        propertys: Map<String, Any>? = null
    ) {
        val url = URL("${address}/${id}")
        val connection = url.openConnection() as HttpURLConnection

        result = try {
            connection.requestMethod = "PUT"

            if (token != null) connection.addRequestProperty("Authorization", "Bearer $token")
            if (propertys != null) setPropertysToConnection(connection, propertys)

            setDataBody(connection, parameters)

            val inputStream = connection.inputStream
            code = connection.responseCode

            inputStream.bufferedReader().use(BufferedReader::readText)
        } catch (error: Exception) {
            code = -1
            NOT_CONNECTED
        } finally {
            connection.disconnect()
        }
    }

    fun delete(
        address: String,
        id: String,
        token: String? = null,
        propertys: Map<String, Any>? = null
    ) {
        val url = URL("${address}/${id}")
        val connection = url.openConnection() as HttpURLConnection

        result = try {
            connection.requestMethod = "DELETE"

            if (token != null) connection.addRequestProperty("Authorization", "Bearer $token")
            if (propertys != null) setPropertysToConnection(connection, propertys)

            val inputStream = connection.inputStream
            code = connection.responseCode

            inputStream.bufferedReader().use(BufferedReader::readText)
        } catch (error: Exception) {
            code = -1
            NOT_CONNECTED
        } finally {
            connection.disconnect()
        }
    }

    private fun setPropertysToConnection(connection: HttpURLConnection, propertys: Map<String, Any>) {
        for ((key, value ) in propertys) { connection.setRequestProperty(key, value.toString()) }
    }

    private fun setDataBody(connection: HttpURLConnection, parameters: Map<String, Any>) {
        val builder = StringBuilder()

        for ((key, value) in parameters) {
            if (builder.isNotEmpty()) builder.append("&")
            builder
                .append(URLEncoder.encode(key, "UTF-8"))
                .append("=")
                .append(URLEncoder.encode(value.toString(), "UTF-8"))
        }

        val dataBytes = builder.toString().toByteArray(charset("UTF-8"))

        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        connection.setRequestProperty("Content-Length", dataBytes.size.toString())
        connection.doOutput = true
        connection.outputStream.write(dataBytes)
    }

    companion object {
        private const val NOT_CONNECTED = "{\"error\": \"not connected\"}"
    }
}
