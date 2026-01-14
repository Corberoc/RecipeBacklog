package com.example.recipebacklog.data.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json // _____Clément_____

object ApiClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { // _____Clément_____
                ignoreUnknownKeys = true // _____Clément_____
            })
        }
    }
}
