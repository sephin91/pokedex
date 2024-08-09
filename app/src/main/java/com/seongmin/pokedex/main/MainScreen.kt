package com.seongmin.pokedex.main

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.seongmin.pokedex.base.LifecycleHandler
import com.seongmin.pokedex.base.OnLifecycleEvent
import com.seongmin.pokedex.base.OnViewSideEffectChanged
import com.seongmin.pokedex.data.model.PokemonIndex
import com.seongmin.pokedex.ui.theme.Black
import com.seongmin.pokedex.ui.theme.PokeDexTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()

    OnLifecycleEvent(
        handler = LifecycleHandler(
            onCreate = {
                viewModel.setEvent(event = MainContract.Event.OnCreate)
            }
        )
    )

    OnViewSideEffectChanged(sideEffect = viewModel.sideEffect) { sideEffect ->
        when (sideEffect) {
            is MainContract.SideEffect.MoveToDetail -> {}
        }
    }

    PokeDexTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Grid(
                modifier = Modifier
                    .padding(paddingValues = innerPadding)
                    .fillMaxSize(),
                items = pagingData,
                onEventSent = viewModel::setEvent
            )
        }
    }
}

@Composable
fun Grid(
    modifier: Modifier,
    items: LazyPagingItems<PokemonIndex>,
    onEventSent: (MainContract.Event) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier.padding(all = 8.dp),
        columns = GridCells.Fixed(count = 2),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        items(count = items.itemCount) { index ->
            PokemonIndex(
                modifier = Modifier.fillMaxWidth(),
                pokemonIndex = items[index] ?: PokemonIndex(),
                onClick = { pokemonIndex ->
                    onEventSent(MainContract.Event.OnClickPokemonIndex(pokemonIndex = pokemonIndex))
                }
            )
        }
    }
}

@Composable
fun PokemonIndex(
    modifier: Modifier,
    pokemonIndex: PokemonIndex,
    onClick: (PokemonIndex) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var bitmap by remember { mutableStateOf<Bitmap?>(value = null) }
    var palette by remember { mutableStateOf<Palette?>(value = null) }

    LaunchedEffect(pokemonIndex.imageUrl) {
        val request = ImageRequest.Builder(context = context)
            .data(data = pokemonIndex.imageUrl)
            .allowHardware(enable = false)
            .build()

        val result = (context.imageLoader.execute(request = request) as? SuccessResult)?.drawable

        bitmap = (result as? BitmapDrawable)?.bitmap

        coroutineScope.launch(context = Dispatchers.Default) {
            bitmap?.let {
                palette =
                    Palette.from(it)
                        .generate()
            }
        }
    }

    val isDarkMode = isSystemInDarkTheme()

    val dominantColor = palette.getColor(defaultColor = MaterialTheme.colorScheme.background) {
        val color = Color(color = getDominantColor(it))
        if (isDarkMode) {
            getBlendedColor(
                originColor = Color(color = getDominantColor(it)),
                blendColor = Black,
                ratio = 0.3f
            )
        } else {
            color
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(ratio = 1f)
            .background(
                color = dominantColor,
                shape = RoundedCornerShape(size = 8.dp)
            )
            .clickable {
                onClick(pokemonIndex)
            }
    ) {
        val imageModifier = Modifier
            .fillMaxSize()
            .weight(1f)

        bitmap?.let {
            Image(
                modifier = imageModifier,
                bitmap = it.asImageBitmap(),
                contentDescription = null
            )
        } ?: Spacer(modifier = imageModifier)

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = 8.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
            maxLines = 1,
            text = pokemonIndex.displayName,
            color = getTextColorForBackground(backgroundColor = dominantColor),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun getTextColorForBackground(backgroundColor: Color): Color {
    val r = (backgroundColor.red * 255).toInt()
    val g = (backgroundColor.green * 255).toInt()
    val b = (backgroundColor.blue * 255).toInt()

    val luminance = 0.299 * r + 0.587 * g + 0.114 * b

    return if (luminance > 128) Color.Black else Color.White
}

private fun Palette?.getColor(
    defaultColor: Color,
    block: Palette.(Int) -> Color
): Color {
    return this?.block(defaultColor.toArgb()) ?: defaultColor
}

fun getBlendedColor(
    originColor: Color,
    blendColor: Color,
    ratio: Float
): Color {
    val inverseRatio = 1 - ratio
    val red = (originColor.red * ratio) + (blendColor.red * inverseRatio)
    val green = (originColor.green * ratio) + (blendColor.green * inverseRatio)
    val blue = (originColor.blue * ratio) + (blendColor.blue * inverseRatio)
    val alpha = (originColor.alpha * ratio) + (blendColor.alpha * inverseRatio)
    return Color(
        red,
        green,
        blue,
        alpha
    )
}