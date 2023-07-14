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
import com.example.notesapp.databinding.FragmentRegisterBinding
import com.example.notesapp.models.UserRequest
import com.example.notesapp.utils.LoadingDialog
import com.example.notesapp.utils.NetworkResult
import com.example.notesapp.utils.TokenManager
import com.example.notesapp.viewmodels.AuthViewModel
import com.example.notesapp.views.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    @Inject
    lateinit var tokenManager: TokenManager
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var loadingDialog: LoadingDialog
    private var userEmail: String? = null
    private var userName: String? = null
    private var userPassword: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        loadingDialog = LoadingDialog((activity as MainActivity))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        clickListeners()
        bindObservers()
    }

    //click listeners initialise
    private fun clickListeners() {
        //signup button
        binding.signupButton.setOnClickListener {

            val validationResult = validateUserInput()
            if (validationResult.first) {
                loadingDialog.startLoading()
                authViewModel.userRegister(UserRequest(userName, userEmail, userPassword))
            } else {
                binding.errorTextView.text = validationResult.second
                binding.errorTextView.visibility = View.VISIBLE
            }

        }
        //back to login button
        binding.backToLogin.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    //observers method
    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            loadingDialog.isDismiss()
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token.toString())
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }

                is NetworkResult.Error -> {
                    binding.errorTextView.text = it.message
                    binding.errorTextView.visibility = View.VISIBLE
                }

                is NetworkResult.Loading -> {
                    loadingDialog.startLoading()
                }
            }
        })
    }

    //input user validation method
    private fun validateUserInput(): Pair<Boolean, String> {
        userEmail = binding.etSignupEmail.text.toString()
        userName = binding.etSignupName.text.toString()
        userPassword = binding.etSignupPassword.text.toString()
        return authViewModel.validateInput(userEmail!!, userName!!, userPassword!!, false)

    }

}