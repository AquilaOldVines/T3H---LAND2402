package com.example.btth_t3h_13

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ChatAdapter(private val messages: List<Message>, private val demo : TestCallBack)  : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        private const val VIEW_TYPE_TEXT = 1
        private const val VIEW_TYPE_IMAGE = 2
    }

    override fun getItemViewType(position: Int): Int {

        return when (messages[position]) {

            is Message.TextMessage -> VIEW_TYPE_TEXT
            is Message.ImageMessage -> VIEW_TYPE_IMAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_TEXT) {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_text_message, parent, false)
            TextMessageViewHolder(view)

        } else {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_message, parent, false)
            ImageMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {

            is TextMessageViewHolder -> holder.bind(messages[position] as Message.TextMessage)

            is ImageMessageViewHolder -> holder.bind(messages[position] as Message.ImageMessage)
        }
    }

    override fun getItemCount(): Int = messages.size

     class TextMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val messageText: TextView = itemView.findViewById(R.id.messageText)

        fun bind(message: Message.TextMessage) {

            messageText.text = message.text
        }
    }

    class ImageMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView: ImageView = itemView.findViewById(R.id.messageImage)

        fun bind(message: Message.ImageMessage) {

            Glide.with(itemView.context).load(message.imageUri).into(imageView)

            imageView.setOnClickListener{

                // todo
            }
        }
    }

    interface TestCallBack{

        fun XoaAnh()
    }
}