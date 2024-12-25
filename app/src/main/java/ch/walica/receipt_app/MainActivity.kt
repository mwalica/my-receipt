package ch.walica.receipt_app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ch.walica.receipt_app.camera_screen.CameraScreen
import ch.walica.receipt_app.main_screen.MainScreen
import ch.walica.receipt_app.ui.theme.Receipt_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasRequiredPermission()) {
            ActivityCompat.requestPermissions(this, CAMERAX_PERMISSION, 0)
        }

        enableEdgeToEdge()
        setContent {
            Receipt_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun hasRequiredPermission(): Boolean {
        return CAMERAX_PERMISSION.all { permission ->
            ContextCompat.checkSelfPermission(
                applicationContext, permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSION = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    }
}

