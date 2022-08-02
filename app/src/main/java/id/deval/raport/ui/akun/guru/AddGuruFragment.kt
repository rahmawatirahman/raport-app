package id.deval.raport.ui.akun.guru

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import id.deval.raport.R
import id.deval.raport.databinding.FragmentAddGuruBinding
import id.deval.raport.utils.BaseSkeletonFragment
import id.deval.raport.utils.HelperView

class AddGuruFragment : BaseSkeletonFragment() {

    private lateinit var _binding : FragmentAddGuruBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGuruBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            ivAddguruBack.setOnClickListener {
                mainNavController.popBackStack()
            }
            mbAddguruSimpan.setOnClickListener {
                mainNavController.popBackStack()
            }
        }
    }
}