package com.example.groceryuserapp.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.groceryuserapp.R
import com.example.groceryuserapp.Utils
import com.example.groceryuserapp.activity.AuthMainActivity
import com.example.groceryuserapp.databinding.AddressBookLayoutBinding
import com.example.groceryuserapp.databinding.FragmentProfileBinding
import com.example.groceryuserapp.viewmodels.UserViewModel


class ProfileFragment : Fragment() {
    private val viewModel : UserViewModel by viewModels()
    private lateinit var binding : FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        onBackButtonClick()
        onOrdersLayoutCick()
        onAddressBtnClick()
        onLogOut()
        return binding.root
    }

    private fun onLogOut() {
        binding.llLogOut.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val alertDialog = builder.create()
                builder.setTitle("Log Out")
                .setMessage("Do you want to log out ?")
                .setPositiveButton("Yes"){_,_->
                    viewModel.logOutUser()
                    startActivity(Intent(requireContext(), AuthMainActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("No"){_,_->
                    alertDialog.dismiss()
                }
                    .show()
                    .setCancelable(false)
        }
    }

    private fun onAddressBtnClick() {
        binding.llAddress.setOnClickListener {
            val addressLayoutBinding = AddressBookLayoutBinding.inflate(LayoutInflater.from(requireContext()))
            viewModel.getUserAddress {address->
            addressLayoutBinding.etAddress.setText(address.toString())
            }
            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(addressLayoutBinding.root)
                .create()
            alertDialog.show()

            addressLayoutBinding.btnEdit.setOnClickListener {
                addressLayoutBinding.etAddress.isEnabled=true
            }
            addressLayoutBinding.btnSave.setOnClickListener {
                viewModel.saveAddress(addressLayoutBinding.etAddress.text.toString())
                alertDialog.dismiss()
                Utils.showToast(requireContext(), "Address Updated..")
            }
        }
    }

    private fun onOrdersLayoutCick() {
        binding.llOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_ordersFragment)
        }
    }

    private fun onBackButtonClick() {
        binding.tbProfileFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }
    }

}