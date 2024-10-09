package com.azzam.protectmyfiles.presentation.navigation

sealed class Screens(val route: String, val title: String) {
    data object SplashScreen : Screens("/splash/", "Splash")
    data object KeyPadScreen : Screens("/keypad/", "Keypad")
    data object HomeScreen : Screens("/home/", "Home")
    data object EncryptedFileListScreen : Screens("/encrypted_file_list/", "Encrypted File List")
    data object SavedPasswordsScreen : Screens("/saved_passwords_screen/", "Saved Passwords")

    fun withArgs(vararg args: String?): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("$arg/")
            }
        }
    }
}
