package com.seongmin.pokedex.main

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import com.seongmin.pokedex.data.model.Pokemon
import com.seongmin.pokedex.data.model.Stats
import com.seongmin.pokedex.data.model.Types

@Composable
fun PokemonDetailBottomSheet(
    modifier: Modifier,
    pokemon: Pokemon,
    isVisible: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PokemonImage(
            modifier = Modifier,
            pokemon = pokemon
        )

        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            maxLines = 1,
            text = pokemon.displayName,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineLarge,
            color = getTextColorForBackground(backgroundColor = Color(pokemon.dominantColor))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Types(
                modifier = Modifier.fillMaxWidth(),
                types = pokemon.types
            )

            HeightAndWeight(
                modifier = Modifier.fillMaxWidth(),
                height = pokemon.height,
                weight = pokemon.weight
            )

            Stats(
                modifier = Modifier.fillMaxWidth(),
                stats = pokemon.stats,
                isVisible = isVisible
            )
        }
    }
}

@Composable
private fun PokemonImage(
    modifier: Modifier,
    pokemon: Pokemon
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
    ) {
        AsyncImage(
            modifier = Modifier.align(alignment = Alignment.Center),
            model = pokemon.imageUrl,
            contentDescription = null,
            imageLoader = context.imageLoader
        )

        Text(
            modifier = Modifier.align(alignment = Alignment.TopEnd),
            maxLines = 1,
            text = "#%03d".format(pokemon.id),
            color = getTextColorForBackground(backgroundColor = Color(pokemon.dominantColor)),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun Types(
    modifier: Modifier,
    types: List<Types>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(
            8.dp,
            Alignment.CenterHorizontally
        )
    ) {
        types.map(Types::type)
            .forEach { type ->
                val backgroundColor = Color(android.graphics.Color.parseColor(type.color))

                Text(
                    modifier = Modifier
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(percent = 100)
                        )
                        .padding(
                            vertical = 4.dp,
                            horizontal = 8.dp
                        ),
                    text = type.name,
                    color = getTextColorForBackground(backgroundColor = backgroundColor)
                )
            }
    }
}

@Composable
private fun HeightAndWeight(
    modifier: Modifier,
    height: Int,
    weight: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(
            space = 32.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 4.dp)
        ) {
            Text(
                text = "Height",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.background)
            )
            Text(
                text = "${height}ft",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.background)
            )
        }

        VerticalDivider(
            modifier = Modifier.height(height = 30.dp),
            color = Color.LightGray
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 4.dp)
        ) {
            Text(
                text = "Weight",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.background)
            )
            Text(
                text = "${weight}lb",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.background)
            )
        }
    }
}

@Composable
private fun Stats(
    modifier: Modifier,
    stats: List<Stats>,
    isVisible: Boolean
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stats.forEach { stat ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 4.dp,
                    alignment = Alignment.End
                )
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(fraction = 0.15f),
                    text = stat.displayName,
                    color = MaterialTheme.colorScheme
                        .contentColorFor(
                            backgroundColor = MaterialTheme.colorScheme
                                .background
                        )
                )

                BoxWithConstraints {
                    val statWidth by animateDpAsState(
                        targetValue = if (isVisible) {
                            maxWidth * stat.ratio
                        } else {
                            0.dp
                        },
                        animationSpec = tween(durationMillis = 500)
                    )

                    Box(
                        Modifier
                            .width(width = maxWidth)
                            .height(height = 20.dp)
                            .background(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(percent = 100)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .width(width = statWidth)
                                .fillMaxHeight()
                                .background(
                                    color = Color(
                                        android.graphics.Color.parseColor(
                                            stat.color
                                        )
                                    ),
                                    shape = RoundedCornerShape(percent = 100)
                                )
                        ) {
                            Text(
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterEnd)
                                    .padding(end = 4.dp),
                                text = "${stat.base_stat}/${stat.maxStat}",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}