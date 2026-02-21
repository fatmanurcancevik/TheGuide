package com.example.theguide

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.net.URLEncoder


class CityApi(
    private val apiKey: String
) {
    private val client = OkHttpClient()

    fun searchCityByName(name: String): List<City> {
        val url = "https://api.api-ninjas.com/v1/city?name=${name.trim()}"

        val request = Request.Builder()
            .url(url)
            .addHeader("X-Api-Key", apiKey)
            .build()

        client.newCall(request).execute().use { resp ->
            if (!resp.isSuccessful) error("HTTP ${resp.code}")

            val body = resp.body?.string().orEmpty()
            val arr = JSONArray(body)

            val result = mutableListOf<City>()
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                result += City(
                    name = o.optString("name"),
                    country = o.optString("country"),
                    region = o.optString("region"),
                    latitude = if (o.has("latitude")) o.optDouble("latitude") else null,
                    longitude = if (o.has("longitude")) o.optDouble("longitude") else null,
                    population = if (o.has("population")) o.optInt("population") else null
                )
            }
            return result
        }
    }
    fun getCitiesByQuery(queryString: String): List<City> {
        val url = "https://api.api-ninjas.com/v1/city?$queryString"

        val request = Request.Builder()
            .url(url)
            .addHeader("X-Api-Key", apiKey)
            .build()

        client.newCall(request).execute().use { resp ->
            if (!resp.isSuccessful) throw Exception("HTTP ${resp.code}")

            val body = resp.body?.string().orEmpty()
            val arr = JSONArray(body)

            val result = mutableListOf<City>()
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                result += City(
                    name = o.optString("name"),
                    country = o.optString("country"),
                    region = o.optString("region"),
                    latitude = if (o.has("latitude")) o.optDouble("latitude") else null,
                    longitude = if (o.has("longitude")) o.optDouble("longitude") else null,
                    population = if (o.has("population")) o.optInt("population") else null
                )
            }
            return result
        }
    }
}