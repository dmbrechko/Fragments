package com.example.fragments.details

import android.os.Bundle
import android.util.Log
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
import com.example.fragments.MainAction
import com.example.fragments.MainActivity
import com.example.fragments.MainViewModel
import com.example.fragments.R
import com.example.fragments.databinding.FragmentDetailsBinding
import com.example.fragments.room.Note
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class DetailsFragment : Fragment() {
    private val viewModel by viewModels<DetailsViewModel> {
        DetailsModelFactory(requireActivity().application, arguments?.getLong(NOTE_ID)!!)
    }
    private val activityViewModel by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentDetailsBinding
    private val context
        get() = requireActivity()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
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
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_nav_24)
            toolbar.setNavigationOnClickListener {
                activityViewModel.makeAction(MainAction.RemoveDetailsFragment)
            }
            toolbar.title = requireActivity().getString(R.string.details_title)
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.noteFlow.map { it.text }.collect {
                        if (noteTextET.text.toString() != it) {
                            noteTextET.setText(it)
                        }
                    }
                }
            }
            saveBTN.setOnClickListener {
                viewModel.note?.let {
                    if (noteTextET.text.toString() != it.text) {
                        Log.d(TAG, "changing text in database")
                        viewModel.updateNote(it.copy(text = noteTextET.text.toString()))
                    }
                }
            }
        }
    }

    companion object {
        private const val NOTE_ID = "note id"
        private const val TAG = "DetailsFragment"
        @JvmStatic
        fun newInstance(noteId: Long) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(NOTE_ID, noteId)
                }
            }
    }
}