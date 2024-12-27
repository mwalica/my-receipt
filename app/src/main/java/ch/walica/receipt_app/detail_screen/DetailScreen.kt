package ch.walica.receipt_app.detail_screen

import android.provider.MediaStore
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import ch.walica.receipt_app.utlis.contentResolverQuery
import coil.compose.AsyncImage

@Composable
fun DetailScreen(modifier: Modifier = Modifier, navController: NavController, id: Long) {

    val context = LocalContext.current

    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.TITLE
    )

    val selection = "${MediaStore.Images.Media._ID} == ?"
    val selectionArgs = arrayOf("$id")

    val image = contentResolverQuery(context, projection, selection, selectionArgs, null)
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = image[0].uri,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.align(alignment = Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Go back to Main"
            )
        }
    }
}