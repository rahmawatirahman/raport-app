package id.deval.raport.ui.pesan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import id.deval.raport.R
import id.deval.raport.databinding.FragmentPesanBinding
import id.deval.raport.db.models.Mapel
import id.deval.raport.db.models.Siswa
import id.deval.raport.ui.adapter.RvAdapter
import id.deval.raport.utils.BaseSkeletonFragment
import id.deval.raport.utils.DummyData
import id.deval.raport.utils.OperationsTypeRv
import id.deval.raport.utils.invisible

class PesanFragment : BaseSkeletonFragment() {

    private lateinit var _binding : FragmentPesanBinding
    private val binding get() = _binding
    private lateinit var dataMapel: ArrayList<Mapel>
    private lateinit var dataSiswa: ArrayList<Siswa>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPesanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataMapel = DummyData().setDummyDataMapel()
        dataSiswa = DummyData().setDummyDataSiswa()
        with(binding){
            mtvPesanMapel.text = dataMapel.size.toString()
            mtvPesanSiswa.text = dataSiswa.size.toString()

            includeRvSiswa.mtvRvlayoutTitle.text = "Siswa"
            includeRvSiswa.mtvRvlayoutAdd.invisible()
            includeRvSiswa.mtvRvlayoutViewmore.invisible()
            includeRvSiswa.rvRvlayoutContainer.apply {
                val adapter = RvAdapter<Siswa>("siswa-pesan", OperationsTypeRv.READ, mainNavController)
                adapter.setData(dataSiswa)
                adapter.notifyDataSetChanged()
                this.adapter = adapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }
    }

}