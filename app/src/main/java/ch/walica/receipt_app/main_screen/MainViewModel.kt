package ch.walica.receipt_app.main_screen

import androidx.lifecycle.ViewModel
import ch.walica.receipt_app.models.Image
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _images = MutableStateFlow<List<Image>>(emptyList())
    val images = _images.asStateFlow()

    fun updateImage(images: List<Image>) {
        _images.update {
            images
        }
    }
}