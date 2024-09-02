package com.example.btth_t3h_13

import android.net.Uri

sealed class Message {

    data class TextMessage(val text: String) : Message()
    data class ImageMessage(val imageUri: Uri) : Message()
}