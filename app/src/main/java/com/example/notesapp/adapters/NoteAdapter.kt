
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.NoteItemBinding
import com.example.notesapp.models.NoteResponse

class NoteAdapter(private val context:onItemClickListener) :
    ListAdapter<NoteResponse, NoteAdapter.NoteViewHolder>(ComparatorDiffUtil()) {
    private var listener: onItemClickListener
    init {
        listener = context

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(it)
        }
    }

    inner class NoteViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NoteResponse) {
            binding.noteTitle.text = note.title
            binding.noteDesc.text = note.description
            binding.root.setOnClickListener{
                listener.onClick(note)
            }
        }

    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<NoteResponse>() {
        override fun areItemsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem == newItem
        }
    }

    interface onItemClickListener{
        fun onClick(noteResponse: NoteResponse)
    }
}
