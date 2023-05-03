import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.singlealbum.MediaLifecycleObserve
import ru.netology.singlealbum.R
import ru.netology.singlealbum.databinding.MusicBinding
import ru.netology.singlealbum.dto.Song

private val url =
    "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/"

interface OnInteractionListener {
    fun play(song: Song) {}
    fun pause(song: Song) {}
}

class TracksAdapter(
    private val mediaObserver: MediaLifecycleObserve,
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Song, TracksViewHolder>(TracksDiffCallback()) {

    fun nextId(position: Int): Int {
        return if (position == currentList.size) 0 else position
    }

    fun nextSong(position: Int): Song {
        return getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val binding = MusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TracksViewHolder(binding, mediaObserver, onInteractionListener)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        val tracks = getItem(position)
        holder.bind(tracks)
    }
}

class TracksViewHolder(
    private val binding: MusicBinding,
    private val mediaObserver: MediaLifecycleObserve,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(song: Song) {
        binding.apply {
            songName.text = song.file
            albumName.text = song.album

            playButton.setOnClickListener{
                onInteractionListener.play(song)
            }

            playButton.icon = if (song.playTracks) AppCompatResources.getDrawable(
                playButton.context,
                R.drawable.baseline_pause_circle_24
            ) else
                AppCompatResources.getDrawable(
                    playButton.context,
                    R.drawable.baseline_play_circle_filled_24
                )
        }
    }
}

class TracksDiffCallback : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }
}