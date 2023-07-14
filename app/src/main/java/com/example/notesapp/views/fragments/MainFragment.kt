package com.example.notesapp.views.fragments

import NoteAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.R
import com.example.notesapp.api.NotesAPI
import com.example.notesapp.databinding.FragmentMainBinding
import com.example.notesapp.models.NoteResponse
import com.example.notesapp.utils.Constants.TAG
import com.example.notesapp.utils.LoadingDialog
import com.example.notesapp.utils.NetworkResult
import com.example.notesapp.viewmodels.NoteViewModel
import com.example.notesapp.views.activities.MainActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(), NoteAdapter.onItemClickListener {

    private var _binding: FragmentMainBinding?=null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        noteAdapter = NoteAdapter(this)
        loadingDialog = LoadingDialog((activity as MainActivity))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        clickListeners()
        loadingDialog.startLoading()
        bindObservers()
        noteViewModel.getAllNotes()
        binding.noteRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noteRecyclerView.adapter = noteAdapter
    }

    private fun bindObservers(){
        loadingDialog.isDismiss()
        noteViewModel.notesLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success->{
                    noteAdapter.submitList(it.data)
                    loadingDialog.isDismiss()
                }

                is NetworkResult.Error->{
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading->{
                    loadingDialog.startLoading()
                }


            }
        })
    }

    /**
     * all click listeners
     */
    private fun clickListeners(){
        binding.addNoteBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }
    }

    override fun onClick(noteResponse: NoteResponse) {
//        Toast.makeText(requireContext(), "note-> ${noteResponse}", Toast.LENGTH_LONG).show()
        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(noteResponse))
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)
    }
}