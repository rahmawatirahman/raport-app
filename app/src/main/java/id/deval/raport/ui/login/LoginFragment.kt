package id.deval.raport.ui.login

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import id.deval.raport.R
import id.deval.raport.databinding.FragmentLoginBinding
import id.deval.raport.utils.BaseSkeletonFragment
import id.deval.raport.utils.HelperView
import id.deval.raport.utils.getMainNavController

class LoginFragment : BaseSkeletonFragment() {

    private lateinit var _binding : FragmentLoginBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            mbLoginLogin.setOnClickListener {
                mainNavController.navigate(R.id.action_loginFragment_to_baseFragment)
            }
        }
    }

}