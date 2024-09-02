package musicview

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.btth_t3h_14.databinding.MusicViewBinding
import data.Song


class ListMusicView(private val items : List<Song>, private val callbackId : PhatBaiMoi) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding : ViewBinding
        val inflater = LayoutInflater.from(parent.context)

        binding = MusicViewBinding.inflate(inflater,parent,false)
        return GroupSong(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is GroupSong){
            holder.bind(items[position])
        }
    }

    inner class GroupSong(val binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root){

        var check : Int = 1;
        val mMediaPlayer = MediaPlayer()

        fun bind(item: Song){

            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

            with(binding){

                textviewNameSong.text = item.nameSong
                textviewSingerSong.text = item.singerSong

                playSong.setOnClickListener {

                    callbackId.playNewSong(item, mMediaPlayer)
                    check = 1 - check

                    if (check ==0){

                        mMediaPlayer.setOnPreparedListener(OnPreparedListener { mediaPlayer -> mediaPlayer.start() })

                    } else {

                        mMediaPlayer.setOnPreparedListener(OnPreparedListener { mediaPlayer -> mediaPlayer.pause() })

                    }
                }
            }
        }
    }

    interface PhatBaiMoi{

        fun playNewSong(item: Song, mMediaPlayer : MediaPlayer)
    }
}