package ch.walica.receipt_app.camera_screen

sealed interface CameraScreenAction {
    data object ShowAlert : CameraScreenAction
    data object HideAlert : CameraScreenAction
    data class TextFieldChange(val newText: String) : CameraScreenAction
}