package ch.walica.receipt_app.main_screen

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.walica.receipt_app.ui.theme.Receipt_appTheme
import coil.compose.AsyncImage
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MainScreen(modifier: Modifier = Modifier, mainViewModel: MainViewModel = viewModel()) {

    val context = LocalContext.current

    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.TITLE
    )

    val selection = "${MediaStore.Images.Media.RELATIVE_PATH} == ?"
    val selectionArgs = arrayOf("Pictures/Receipt/")

    context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        null
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
        val nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
        val addDateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
        val titleColumn = cursor.getColumnIndex(MediaStore.Images.Media.TITLE)

        val images = mutableListOf<Image>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val addDate = cursor.getLong(addDateColumn)
            val title = cursor.getString(titleColumn)
            val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            images.add(Image(id, addDate, name, uri, title))
        }

        mainViewModel.updateImage(images)
    }


    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(items = mainViewModel.images.value) { image ->

            val date = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(image.addDate),
                ZoneId.systemDefault()
            )

            val formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date)

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = image.uri,
                    contentDescription = null,
                    modifier = Modifier.width(120.dp)
                )
                Text(text = image.name)
                Text(text = image.title)
            }
        }
    }

}

data class Image(
    val id: Long,
    val addDate: Long,
    val name: String,
    val uri: Uri,
    val title: String
)

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    Receipt_appTheme {
        MainScreen()
    }

}