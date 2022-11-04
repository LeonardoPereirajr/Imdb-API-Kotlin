package br.com.alura

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import br.com.alura.model.Movie
import br.com.alura.webclient.MovieWebClient
import org.jetbrains.skia.Image
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.net.URL
import javax.imageio.ImageIO


@Composable
@Preview
fun App(movies: List<Movie>) {
    MaterialTheme(
        colors = darkColors()
    ) {
        Surface {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn {
                    items(movies) { movie ->
                        MovieItem(
                            movie
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieItem(movie: Movie) {
    Column(
        modifier = Modifier
            .width(800.dp)
            .padding(16.dp)
    ) {
        Image(
            bitmap = movie.image.loadImageBitmap(),
            contentDescription = "capa do filme",
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(
                    top = 8.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star,
                    "ícone de estrela para nota",
                    tint = Color.Yellow,
                    modifier = Modifier.height(16.dp)
                )
                Text(
                    movie.rating.toString(),
                    modifier = Modifier
                        .padding(start = 2.dp),
                    color = Color(0xffeeeeee),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                movie.year.toString(),
                fontSize = 14.sp,
                color = Color(0xffeeeeee),
            )
        }
        Text(
            movie.title,
            modifier = Modifier.padding(
                start = 16.dp,
                top = 8.dp,
                end = 16.dp
            ),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

fun String.loadImageBitmap(): ImageBitmap {
    val inputStream = BufferedInputStream(
        URL(this).openStream()
    )
    val bufferedImage = ImageIO.read(inputStream)

    val stream = ByteArrayOutputStream()
    ImageIO.write(bufferedImage, "png", stream)
    val byteArray = stream.toByteArray()

    return Image.makeFromEncoded(byteArray).toComposeImageBitmap()
}

fun main() = application {
    val client = MovieWebClient()
    var movies: List<Movie> by remember {
        mutableStateOf(emptyList())
    }
    client.findTop250Movies {
        movies = it
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "IMDB"
    ) {
        App(movies)
    }
}
