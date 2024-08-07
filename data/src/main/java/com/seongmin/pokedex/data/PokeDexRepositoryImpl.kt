package com.seongmin.pokedex.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.seongmin.pokedex.data.model.Const
import com.seongmin.pokedex.data.model.PokemonIndex
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokeDexRepositoryImpl @Inject constructor(private val pokeDexDataSource: PokeDexDataSource) :
    PokeDexRepository {
    override suspend fun getPokemonIndexInfo(): Flow<PagingData<PokemonIndex>> {
        return Pager(
            config = PagingConfig(
                pageSize = Const.MAX_COUNT,
                prefetchDistance = Const.PREFETCH_DISTANCE
            ),
            pagingSourceFactory = {
                PokeDexPagingSource(pokeDexDataSource)
            }
        ).flow
    }
}