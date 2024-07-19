package com.seongmin.pokedex.main

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.seongmin.pokedex.base.LifecycleHandler
import com.seongmin.pokedex.base.OnLifecycleEvent
import com.seongmin.pokedex.data.model.PokemonIndex
import com.seongmin.pokedex.ui.theme.PokeDexTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsState()

    OnLifecycleEvent(
        handler = LifecycleHandler(
            onCreate = {
                viewModel.setEvent(event = MainContract.Event.OnCreate)
            }
        )
    )

    PokeDexTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Grid(
                modifier = Modifier
                    .padding(paddingValues = innerPadding)
                    .fillMaxSize()
                    .background(color = Color.Black),
                items = state.pokemonIndexInfo.results
            )
        }
    }
}

@Composable
fun Grid(
    modifier: Modifier,
    items: List<PokemonIndex>
) {
    LazyVerticalGrid(
        modifier = modifier.padding(all = 8.dp),
        columns = GridCells.Fixed(count = 2),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        items(items = items) { pokemonIndex ->
            PokemonIndex(
                modifier = Modifier.fillMaxWidth(),
                imageUrl = pokemonIndex.imageUrl,
                name = pokemonIndex.displayName
            )
        }
    }
}

@Composable
fun PokemonIndex(
    modifier: Modifier,
    imageUrl: String,
    name: String
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var bitmap by remember { mutableStateOf<Bitmap?>(value = null) }
    var palette by remember { mutableStateOf<Palette?>(value = null) }

    LaunchedEffect(imageUrl) {
        val request = ImageRequest.Builder(context = context)
            .data(data = imageUrl)
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

    val dominantColor = palette.getColor(defaultColor = Color.Black) {
        Color(getDominantColor(it))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(ratio = 1f)
            .background(
                color = dominantColor,
                shape = RoundedCornerShape(size = 8.dp)
            )
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
            text = name,
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
