package com.seongmin.pokedex.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.seongmin.pokedex.data.model.PokemonIndex
import javax.inject.Inject

class PokeDexPagingSource @Inject constructor(private val pokeDexDataSource: PokeDexDataSource) :
    PagingSource<Int, PokemonIndex>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonIndex> {
        return runCatching {
            val currentPage = params.key ?: 1

            val pokeDexIndexList = pokeDexDataSource
                .getPokemonIndexInfo(key = currentPage.dec())
                .results

            LoadResult.Page(
                data = pokeDexIndexList,
                prevKey = if (currentPage == 1) {
                    null
                } else {
                    currentPage.dec()
                },
                nextKey = if (pokeDexIndexList.isEmpty()) {
                    null
                } else {
                    currentPage.inc()
                }
            )
        }.getOrElse { failure ->
            LoadResult.Error(failure)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonIndex>): Int? {
        return state.anchorPosition
    }
}