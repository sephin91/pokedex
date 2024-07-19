package com.seongmin.pokedex.data

import com.seongmin.pokedex.data.model.PokemonIndexInfo
import com.seongmin.pokedex.network.NetworkClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import javax.inject.Inject

class PokeDexRepositoryImpl @Inject constructor(private val networkClient: NetworkClient) :
    PokeDexRepository {
    override suspend fun getPokemonIndexInfo(): PokemonIndexInfo {
        val response = networkClient.client.get {
            url {
                path(POKE_MON_LIST)
                parameter(
                    key = "limit",
                    value = "10000"
                )
            }
        }

        if (response.status.value in 200..299) {
            return response.body()
        } else {
            throw Exception()
        }
    }

    companion object {
        private const val POKE_MON_LIST = "/api/v2/pokemon"
    }
}