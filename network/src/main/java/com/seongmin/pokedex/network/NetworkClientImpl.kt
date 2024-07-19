package com.seongmin.pokedex.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject

internal class NetworkClientImpl @Inject constructor() : NetworkClient {
    override val client: HttpClient = HttpClient(engineFactory = CIO) {
        defaultRequest {
            url(BASE_HOST)
        }

        install(plugin = ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                    encodeDefaults = true
                }
            )
        }
    }

    companion object {
        private const val BASE_HOST = "https://pokeapi.co"
    }
}