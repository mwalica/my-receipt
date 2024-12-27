package ch.walica.receipt_app.camera_screen

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    cameraScreenViewModel: CameraScreenViewModel = viewModel()
) {

    val context = LocalContext.current
    val state by cameraScreenViewModel.state.collectAsStateWithLifecycle()

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE
            )
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CameraPreview(
            cameraController = controller,
            modifier = Modifier.fillMaxSize()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go to Main Screen"
                )
            }
            OutlinedButton(
                modifier = Modifier.padding(end = 8.dp),
                onClick = {
                    cameraScreenViewModel.onAction(CameraScreenAction.ShowAlert)
                }) {
                Text(text = "file name")
            }
        }

        IconButton(onClick = {
            takePhoto(controller, context, state.enteredText)
        }, modifier = Modifier.align(Alignment.BottomCenter)) {
            Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = "Take photo")
        }
    }

    if (state.isAlert) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = "Filename") },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = state.enteredText,
                        onValueChange = {
                            cameraScreenViewModel.onAction(
                                CameraScreenAction.TextFieldChange(it)
                            )
                        })
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    cameraScreenViewModel.onAction(CameraScreenAction.HideAlert)
                }) {
                    Text(text = "Add name")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    cameraScreenViewModel.onAction(CameraScreenAction.HideAlert)
                }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

}

private fun takePhoto(
    controller: LifecycleCameraController,
    context: Context,
    fileName: String
) {
    val defaultName = SimpleDateFormat(
        "yyyy-MM-dd_HH-mm-ss",
        Locale.getDefault()
    ).format(System.currentTimeMillis())

    val name = if (fileName.isBlank()) defaultName else "${fileName}_${(1..1000).random()}"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Receipt")
        }
    }

    val outputOptions = ImageCapture.OutputFileOptions.Builder(
        context.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ).build()

    controller.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val msg = "Photo captured succeeded: ${outputFileResults.savedUri}"
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("my_log", "Couldn't tale photo", exception)
            }

        })
}