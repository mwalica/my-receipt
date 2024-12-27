package ch.walica.receipt_app.main_screen


import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ch.walica.receipt_app.ScreenCamera
import ch.walica.receipt_app.ScreenDetail
import ch.walica.receipt_app.ui.theme.Receipt_appTheme
import ch.walica.receipt_app.utlis.contentResolverQuery
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(),
    navController: NavController
) {

    val context = LocalContext.current

    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.TITLE
    )

    val selection = "${MediaStore.Images.Media.RELATIVE_PATH} == ?"
    val selectionArgs = arrayOf("Pictures/Receipt/")
    val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

    mainViewModel.updateImage(
        contentResolverQuery(
            context,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My Receipt") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(route = ScreenCamera)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add receipt")
            }
        }
    ) { padding ->
        LazyVerticalGrid(
            modifier = Modifier.padding(padding),
            columns = GridCells.Adaptive(152.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = mainViewModel.images.value) { image ->
                Card(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = ScreenDetail(image.id))
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val date = ZonedDateTime.ofInstant(
                            Instant.ofEpochSecond(image.addDate),
                            ZoneId.systemDefault()
                        )
                        val formattedDate = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date)

                        Text(
                            text = formattedDate, style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )
                        Text(
                            text = image.title.substringBefore("_"),
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                }
            }
        }

//        LazyColumn(modifier = Modifier.padding(padding)) {
//            items(items = mainViewModel.images.value) { image ->
//
//                val date = ZonedDateTime.ofInstant(
//                    Instant.ofEpochSecond(image.addDate),
//                    ZoneId.systemDefault()
//                )
//
//                val formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date)
//
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    AsyncImage(
//                        model = image.uri,
//                        contentDescription = null,
//                        modifier = Modifier.width(120.dp)
//                    )
//                    Text(text = image.title.substringBefore("_"))
//                }
//            }
//        }
    }


}


@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    Receipt_appTheme {
        MainScreen(navController = rememberNavController())
    }

}