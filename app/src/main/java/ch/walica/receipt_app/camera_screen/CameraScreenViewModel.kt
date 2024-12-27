package ch.walica.receipt_app.camera_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CameraScreenViewModel : ViewModel() {


    private val _state = MutableStateFlow(CameraScreenState())
    val state = _state.asStateFlow()

    fun onAction(action: CameraScreenAction) {
        when (action) {
            CameraScreenAction.HideAlert -> {
                _state.update {
                    it.copy(
                        isAlert = false
                    )
                }
            }

            CameraScreenAction.ShowAlert -> {
                _state.update {
                    it.copy(
                        isAlert = true
                    )
                }
            }

            is CameraScreenAction.TextFieldChange -> {
                _state.update {
                    it.copy(
                        enteredText = action.newText
                    )
                }
            }
        }
    }
}


