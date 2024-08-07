package com.seongmin.pokedex.data

import com.seongmin.pokedex.data.model.Const
import com.seongmin.pokedex.data.model.PokemonIndexInfo
import com.seongmin.pokedex.network.NetworkClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess
import io.ktor.http.path
import javax.inject.Inject

class PokeDexDataSourceImpl @Inject constructor(private val networkClient: NetworkClient) :
    PokeDexDataSource {
    override suspend fun getPokemonIndexInfo(key: Int): PokemonIndexInfo {
        val response = networkClient.client.get {
            url {
                path(POKE_MON_LIST)
                parameter(
                    key = "limit",
                    value = Const.MAX_COUNT
                )
                parameter(
                    key = "offset",
                    value = Const.MAX_COUNT.times(other = key)
                )
            }
        }


        if (response.status.isSuccess()) {
            return response.body()
        } else {
            throw Exception()
        }
    }

    companion object {
        private const val POKE_MON_LIST = "/api/v2/pokemon"
    }
}