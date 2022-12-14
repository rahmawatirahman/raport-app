package id.deval.raport.ui.note

import android.app.DatePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import id.deval.raport.R
import id.deval.raport.databinding.FragmentAddNoteBinding
import id.deval.raport.db.models.request.NoteAdd
import id.deval.raport.utils.*
import java.util.*

class AddNoteFragment : BaseSkeletonFragment() {

    private lateinit var _binding: FragmentAddNoteBinding
    private val binding get() = _binding
    private var isValid = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pesanId = arguments?.getString(Constanta.ID)
        with(binding) {
            ivAddnoteBack.setOnClickListener {
                try {
                    mainNavController.popBackStack()
                }catch (e: Exception){
                    Log.d(ContentValues.TAG, "onViewCreated: $e")
                }
            }

            tietAddnoteTanggal.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DATE)

                val datePickerDialog =
                    DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener {
                        override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                            val bulan = p2.plus(1).parseIntToMonth()
                            tietAddnoteTanggal.setText("$p3 $bulan $p1")
                        }

                    }, year, month, day)
                datePickerDialog.show()
            }

            tietAddnoteTanggal.hideError()
            tietAddnoteKet.hideError()

            mbAddnoteSimpan.setOnClickListener {
                val date = tietAddnoteTanggal.text.toString()
                val ket = tietAddnoteKet.text.toString()

                if (date.isEmpty()){
                    tietAddnoteTanggal.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }
                if (ket.isEmpty()){
                    tietAddnoteKet.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (ket.isNotEmpty() && date.isNotEmpty()){
                    isValid = true
                }

                if (isValid){
                    val note = NoteAdd(
                        ket, date, pesanId
                    )
                    pesanViewModel.addNote(session.token.toString(),note).observe(viewLifecycleOwner){
                        if (it.isSuccessful){
                            try {
                                requireContext().showToast("${it.body()?.status} menambahkan data")
                                mainNavController.popBackStack()
                            }catch (e: Exception){
                                Log.d(ContentValues.TAG, "onViewCreated: $e")
                            }
                        } else {
                            requireContext().showToast(it.message())
                        }
                    }
                }
            }
        }
    }

}