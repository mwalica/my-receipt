package ch.walica.receipt_app.utlis

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import ch.walica.receipt_app.models.Image

fun contentResolverQuery(
    context: Context,
    projection: Array<String>,
    selection: String,
    selectionArgs: Array<String>,
    sortOrder: String?
): MutableList<Image> {

    val images = mutableListOf<Image>()

    context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
        val nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
        val addDateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
        val titleColumn = cursor.getColumnIndex(MediaStore.Images.Media.TITLE)


        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val addDate = cursor.getLong(addDateColumn)
            val title = cursor.getString(titleColumn)
            val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            images.add(Image(id, addDate, name, uri, title))
        }

    }
    return images
}