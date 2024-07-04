package com.example.groceryadminapp.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.groceryadminapp.activity.AdminMainActivity
import com.example.groceryadminapp.R
import com.example.groceryadminapp.Utils
import com.example.groceryadminapp.databinding.FragmentOTPBinding
import com.example.groceryadminapp.models.Admin
import com.example.groceryadminapp.viewmodels.AuthViewModel
import kotlinx.coroutines.launch


class OTPFragment : Fragment() {
    private val viewModel : AuthViewModel by viewModels()
    private lateinit var binding : FragmentOTPBinding
    private lateinit var userNumber : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOTPBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        getUserNUmber()
        customizeEnteringOTP()
        sendOTP()
        onLoginButtonClick()
        onBackButtonClick()
        return binding.root
    }

    private fun onLoginButtonClick() {
        binding.btnLogin.setOnClickListener {
            Utils.showDialog(requireContext(), "Signing you...")
            val editTexts = arrayOf(binding.etOtp1, binding.etOtp2, binding.etOtp3, binding.etOtp4 , binding.etOtp5, binding.etOtp6)
            val otp = editTexts.joinToString (""){it.text.toString() }

            if (otp.length < editTexts.size){
                Utils.showToast(requireContext(), "Please enter right OTP")
            }
            else{
                editTexts.forEach {it.text?.clear(); it.clearFocus() }
                verifyOtp(otp)
            }
        }
    }

    private fun verifyOtp(otp: String) {
        val user = Admin(uid = null, adminPhoneNumber = userNumber)

        viewModel.signInWithPhoneAuthCredential(otp, userNumber, user)
        lifecycleScope.launch {
            viewModel.isSignedInSuccessfully.collect{
                if (it==true){
                    Utils.hideDialog()
                    Utils.showToast(requireContext(), "Logged In")
                    startActivity(Intent(requireActivity(), AdminMainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

    private fun sendOTP() {
        Utils.showDialog(requireContext(), "Sending OTP...")
        viewModel.apply {
            sendOTP(userNumber, requireActivity())
            lifecycleScope.launch {
                otpSent.collect{
                    if (it==true){
                        Utils.hideDialog()
                        Utils.showToast(requireContext(), "Otp Sent to $userNumber")
                    }
                }
            }
        }
    }

    private fun onBackButtonClick() {
        binding.toOtpFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_OTPFragment_to_signInFragment)
        }
    }

    private fun customizeEnteringOTP() {
        val editTexts = arrayOf(binding.etOtp1, binding.etOtp2, binding.etOtp3, binding.etOtp4 , binding.etOtp5, binding.etOtp6)
        for (i in editTexts.indices){
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if(s?.length==1){
                        if(i< editTexts.size-1){
                            editTexts[i+1].requestFocus()
                        }
                    } else if(s?.length==0){
                        if(i>0){
                            editTexts[i-1].requestFocus()
                        }
                    }
                }
            })
        }
    }

    private fun getUserNUmber() {
        val bundle = arguments
        userNumber = bundle?.getString("number").toString()
        binding.tvUserNo.text = userNumber
    }

}