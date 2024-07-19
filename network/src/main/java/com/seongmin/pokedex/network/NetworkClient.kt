package com.seongmin.pokedex.network

import io.ktor.client.HttpClient

interface NetworkClient {
    val client: HttpClient
}