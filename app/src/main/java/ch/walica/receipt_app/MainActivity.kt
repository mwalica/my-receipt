package ch.walica.receipt_app

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ch.walica.receipt_app.camera_screen.CameraScreen
import ch.walica.receipt_app.detail_screen.DetailScreen
import ch.walica.receipt_app.main_screen.MainScreen
import ch.walica.receipt_app.ui.theme.Receipt_appTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasRequiredPermission()) {
            ActivityCompat.requestPermissions(this, CAMERAX_PERMISSION, 0)
        }

        enableEdgeToEdge()
        setContent {
            Receipt_appTheme {

                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = ScreenMain) {
                        composable<ScreenMain> {
                            MainScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }

                        composable<ScreenCamera> {
                            CameraScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }

                        composable<ScreenDetail> { backStackEntry ->
                            val args = backStackEntry.toRoute<ScreenDetail>()
                            DetailScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController, id = args.id
                            )
                        }
                    }
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

@Serializable
object ScreenMain

@Serializable
object ScreenCamera

@Serializable
data class ScreenDetail(val id: Long)

