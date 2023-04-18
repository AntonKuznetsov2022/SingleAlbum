import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.singlealbum.databinding.MusicBinding
import ru.netology.singlealbum.dto.Tracks

interface OnInteractionListener {
    fun play(tracks: Tracks) {}
}

class TracksAdapter(
    private val OnInteractionListener: OnInteractionListener,
) : ListAdapter <Tracks, TracksViewHolder>(TracksDiffCallback()) {

    fun getId(position: Int): Int {
        return if (position == currentList.size) 0 else position
    }

    fun nextTrack(position: Int): Tracks {
        return getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val binding = MusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TracksViewHolder(binding, OnInteractionListener)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        val tracks = getItem(position)
        holder.bind(tracks)
    }
}

class TracksViewHolder(
    private val binding: MusicBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(tracks: Tracks) {
        binding.apply {
            songName.text = tracks.file
            albumName.text = tracks.album
            play.isCheckable = tracks.playTracks
            play.setOnClickListener {
                onInteractionListener.play(tracks)
            }
        }
    }
}

class TracksDiffCallback : DiffUtil.ItemCallback<Tracks>() {
    override fun areItemsTheSame(oldItem: Tracks, newItem: Tracks): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Tracks, newItem: Tracks): Boolean {
        return oldItem == newItem
    }
}