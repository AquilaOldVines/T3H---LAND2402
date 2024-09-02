package com.example.btth_t3h_13

import android.app.Activity
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.btth_t3h_13.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() , ChatAdapter.TestCallBack {

    private lateinit var binding : ActivityChatBinding

    private val messagesList = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickListener()
    }

    private fun initClickListener(){

        with(binding){

            recyclerView.layoutManager = LinearLayoutManager(this@ChatActivity)
            val adapter = ChatAdapter(messagesList, this@ChatActivity)
            recyclerView.adapter = adapter

            sendButton.setOnClickListener {

                val message = messageInput.text.toString()
                if (message.isNotEmpty()) {

                    messagesList.add(Message.TextMessage(message))
                    adapter.notifyDataSetChanged() // cập nhật dữ liệu
                    messageInput.text.clear()
                }
            }

            val imagePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { item ->

                if (item.resultCode == Activity.RESULT_OK) {

                    val imageUri: Uri? = item.data?.data

                    imageUri?.let {

                        messagesList.add(Message.ImageMessage(it))
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            chatId.setOnClickListener {

                showOverlayView()
            }

            imageButton.setOnClickListener {

                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                imagePicker.launch(intent)
            }
        }
    }

    private fun showOverlayView() {

        if(canDrawOverlay()) {

            // todo draw view overlay
            val buttonOverlay = Button(this)
            buttonOverlay.setText("This this overlay button")

            val lp = WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, //Disables status bar
                PixelFormat.TRANSPARENT) //Transparent
            lp.gravity = Gravity.TOP or Gravity.END
            val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
            windowManager.addView(buttonOverlay, lp)

        } else {

            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                data = Uri.parse("package:$packageName")
            }, 1002)
        }
    }

    private fun canDrawOverlay() : Boolean {

        return Settings.canDrawOverlays(this)
    }

    override fun XoaAnh() {

        // todo xoa anh
    }
}