package com.example.notesapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentLoginBinding
import com.example.notesapp.models.User
import com.example.notesapp.models.UserRequest
import com.example.notesapp.utils.LoadingDialog
import com.example.notesapp.utils.NetworkResult
import com.example.notesapp.utils.TokenManager
import com.example.notesapp.viewmodels.AuthViewModel
import com.example.notesapp.views.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var tokenManager: TokenManager
    private var _binding: FragmentLoginBinding?=null
    private val binding get() =  _binding!!
    private lateinit var loadingDialog: LoadingDialog
    private val authViewModel by viewModels<AuthViewModel>()
    private var userEmail: String? = null
    private var userPassword: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        loadingDialog = LoadingDialog((activity as MainActivity))
        if(tokenManager.getToken() !=null){
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        clickListeners()
        bindObservers()
    }

    private fun clickListeners(){

        //navigate to main fragment
        binding.loginButton.setOnClickListener {

            val validateResult = validateInput()
            if(validateResult.first){
                loadingDialog.startLoading()
                authViewModel.userLogin(UserRequest("", userEmail, userPassword))
            }else{
                binding.errorTextView.text = validateResult.second
                binding.errorTextView.visibility = View.VISIBLE
            }

        }
        //navigate to register fragment
        binding.createAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    //bind observers
    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
        loadingDialog.isDismiss()
            when(it){
                is NetworkResult.Success->{
                    tokenManager.saveToken(it.data!!.token.toString())
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }

                is NetworkResult.Error->{
                    binding.errorTextView.text = it.message
                    binding.errorTextView.visibility = View.VISIBLE
                }

                is NetworkResult.Loading->{
                    loadingDialog.startLoading()
                }
            }
        })
    }

    //validate input
    private fun validateInput():Pair<Boolean, String>{
        userEmail = binding.etLoginEmail.text.toString()
        userPassword = binding.etLoginPassword.text.toString()
        return authViewModel.validateInput(userEmail!!, "", userPassword!!, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}