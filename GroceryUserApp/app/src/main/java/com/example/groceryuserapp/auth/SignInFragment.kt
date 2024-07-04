package com.example.groceryuserapp.auth

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.groceryuserapp.R
import com.example.groceryuserapp.Utils
import com.example.groceryuserapp.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        setStatusBarColor()
        getUserNumber()
        onContinueButtonClick()
        return binding.root
    }

    private fun onContinueButtonClick() {
        binding.btnContnue.setOnClickListener {
            val number = binding.etUserNo.text.toString()
            if(number.isEmpty() || number.length != 10){
                Utils.showToast(requireContext(), "Please Enter Valid Mobile Number")
            }
            else{
                val bundle =Bundle()
                bundle.putString("number", number)
                findNavController().navigate(R.id.action_signInFragment_to_OTPFragment, bundle)
            }
        }
    }

    private fun getUserNumber() {
        binding.etUserNo.addTextChangedListener ( object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val len = number?.length
                if(len== 10){
                    binding.btnContnue.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.royal_orange))
                }
                else{
                    binding.btnContnue.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_orange))
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun setStatusBarColor(){
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.royal_orange)
            statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}