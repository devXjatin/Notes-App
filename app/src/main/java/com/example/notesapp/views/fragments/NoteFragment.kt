package com.example.notesapp.views.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentNoteBinding
import com.example.notesapp.models.NoteRequest
import com.example.notesapp.models.NoteResponse
import com.example.notesapp.utils.LoadingDialog
import com.example.notesapp.utils.NetworkResult
import com.example.notesapp.viewmodels.NoteViewModel
import com.example.notesapp.views.activities.MainActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding?=null
    private val binding get() =_binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private lateinit var loadingDialog: LoadingDialog
    private var noteTitle: String?=null
    private var noteDesc: String?=null
    private var note: NoteResponse?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        loadingDialog = LoadingDialog((activity as MainActivity))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initData()
        clickListeners()
        bindObservers()
    }

    /**
     * initialise Data method
     */

    private fun initData(){
        val jsonData = arguments?.getString("note")
        if(jsonData != null){
            note = Gson().fromJson(jsonData, NoteResponse::class.java)
            note?.let {
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.description)
            }

        }else{
            binding.addEditText.text = "Add Data"
        }

    }
    /**
     * Bind observer
     */

    private fun bindObservers(){
        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success->{
                    loadingDialog.isDismiss()
                    Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
                    Thread.sleep(1500)
                    findNavController().popBackStack()
                }

                is NetworkResult.Error->{}

                is NetworkResult.Error->{}
            }
        })
    }

    /**
     * click listeners
     */
    private fun clickListeners(){
        //back button
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        //delete note
        binding.btnDelete.setOnClickListener {
            if (note!= null){
                loadingDialog.startLoading()
                noteViewModel.deleteNote(note!!.id)
            }
        }

        //submit data button
        binding.btnSubmit.setOnClickListener {
            noteTitle = binding.txtTitle.text.toString()
            noteDesc = binding.txtDescription.text.toString()
            if(checkInput()){
                loadingDialog.startLoading()
                if(note == null){
//                    loadingDialog.startLoading()
                    noteViewModel.createNote(NoteRequest(noteTitle!!, noteDesc!!))
                }else{
                    noteViewModel.updateNote(note!!.id, NoteRequest(noteTitle!!, noteDesc!!))
                }

            }else{
               if(noteTitle.isNullOrEmpty()){
                   binding.txtTitle.setError("Enter Title")
               }
                if(noteDesc.isNullOrEmpty()){
                    binding.txtDescription.setError("Enter Description")
                }
            }
        }
    }

    //Check user input
    private fun checkInput(): Boolean{
        if(TextUtils.isEmpty(noteTitle) || TextUtils.isEmpty(noteTitle)){
            return false
        }
        return true
    }

}