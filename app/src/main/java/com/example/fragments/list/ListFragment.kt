package com.example.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fragments.MainAction
import com.example.fragments.MainViewModel
import com.example.fragments.R
import com.example.fragments.databinding.FragmentListBinding
import com.example.fragments.room.Note
import com.example.fragments.room.NoteToInsert
import kotlinx.coroutines.launch

class ListFragment : Fragment() {
    private val viewModel by viewModels<NotesViewModel>()
    private val activityViewModel by activityViewModels<MainViewModel> ()
    private lateinit var binding: FragmentListBinding
    private val context
        get() = requireActivity()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbar.inflateMenu(R.menu.menu_exit)
            toolbar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.menu_exit) {
                    requireActivity().finish()
                    true
                } else { false }
            }
            saveBTN.setOnClickListener {
                if (noteTextET.text.isBlank()) {
                    Toast.makeText(context, R.string.enter_note_text_please, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val note = NoteToInsert(noteTextET.text.toString())
                viewModel.addNote(note)
                noteTextET.text.clear()
            }
            val actions = object : NotesAdapter.NoteItemActionable {
                override fun onCheckedChanged(isChecked: Boolean, note: Note) {
                    viewModel.updateNote(note.copy(isDone = isChecked))
                }

                override fun onEdit(noteId: Long) {
                    activityViewModel.makeAction(MainAction.ShowDetailsFragment(noteId))
                }

                override fun onDelete(note: Note) {
                    viewModel.deleteNote(note)
                }
            }
            val notesAdapter = NotesAdapter(actions)
            listRV.layoutManager = LinearLayoutManager(context)
            listRV.adapter = notesAdapter
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.notesFlow.collect {
                        notesAdapter.submitList(it)
                    }
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = ListFragment()

        const val TAG = "ListFragment"
    }
}