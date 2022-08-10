package id.deval.raport.ui.akun.siswa

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import id.deval.raport.BuildConfig.BASE_URL
import id.deval.raport.R
import id.deval.raport.databinding.FragmentAddSiswaBinding
import id.deval.raport.db.models.Siswa
import id.deval.raport.utils.BaseSkeletonFragment
import id.deval.raport.utils.Constanta
import id.deval.raport.utils.hide

class AddSiswaFragment : BaseSkeletonFragment() {

    private lateinit var _binding: FragmentAddSiswaBinding
    private val binding get() = _binding
    private lateinit var id: String
    private var isValid = false
    private var pathImage: String? = null
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            Log.d(
                "TAG",
                "requestPermission: ${it[Manifest.permission.READ_EXTERNAL_STORAGE]}  ${it[Manifest.permission.WRITE_EXTERNAL_STORAGE]}"
            )
        }

    private val startPickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data != null && it.data!!.data != null) {
                    val selectedImageUri = it.data!!.data!!
                    val selectedImage = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        selectedImageUri
                    )
                    selectedImageUri.let {
                        val cursor = requireActivity().contentResolver.query(
                            it,
                            arrayOf(MediaStore.Images.Media.DATA),
                            null,
                            null,
                            null
                        )
                        if (cursor == null) {
                            pathImage = selectedImageUri.path.toString()
                        } else {
                            cursor.moveToFirst()
                            val idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                            pathImage = cursor.getString(idx)
                            cursor.close()
                        }
                    }
                    Log.d("TAG", "startPickImageLauncher: $pathImage")
                    binding.ivAddsiswaPhoto.setImageBitmap(selectedImage)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSiswaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val role = arguments?.getString(Constanta.ROLE).toString()
        id = arguments?.getString(Constanta.ID) ?: ""
        with(binding) {
            ivAddsiswaBack.setOnClickListener {
                mainNavController.popBackStack()
            }

            if (id.isNotEmpty()) {
                tietAddsiswaNisn.isEnabled = false
                siswaViewModel.getSiswa(session.token.toString(),id).observe(viewLifecycleOwner){
                    val siswa = it.data
                    tietAddsiswaNamalengkap.setText(siswa.name)
                    tietAddsiswaNisn.setText(siswa.nis)
                    tietAddsiswaTempatlahir.setText(siswa.tempatLahir)
                    tietAddsiswaTanggalLahir.setText(siswa.tanggalLahir)
                    tietAddsiswaGender.setText(siswa.gender)
                    tietAddsiswaReligion.setText(siswa.religion)
                    tietAddsiswaEducation.setText(siswa.education)
                    tietAddsiswaAddress.setText(siswa.address)
                    tietAddsiswaNamaAyah.setText(siswa.namaAyah)
                    tietAddsiswaNamaIbu.setText(siswa.namaIbu)
                    tietAddsiswaPekerjaanAyah.setText(siswa.pekerjaanAyah)
                    tietAddsiswaPekerjaanIbu.setText(siswa.pekerjaanIbu)
                    tietAddsiswaNamaWali.setText(siswa.namaWali)
                    tietAddsiswaPekerjaanWali.setText(siswa.pekerjaanWali)
                    tietAddsiswaAlamatWali.setText(siswa.alamatWali)
                    tietAddsiswaHp.setText(siswa.phone)
                    //IMPLEMENTASI IMAGE
                    val urlPhoto = "${BASE_URL}siswa/file/${siswa.photo}"
                    Glide.with(requireContext())
                        .load(urlPhoto)
                        .into(ivAddsiswaPhoto)
                }
            }

            when (role) {
                "orangtua" -> viewAsOrangtua()
                "admin" -> viewAsAdmin()
            }
        }
    }

    fun viewAsAdmin() {
        with(binding) {
            Log.d("TAG", "viewAsAdmin: ADMIN")
            requestPermission.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )

            ivAddsiswaPhoto.setOnClickListener {
                val intent = Intent()
                intent.type = "image/jpeg"
                intent.action = Intent.ACTION_GET_CONTENT
                startPickImageLauncher.launch(intent)
            }

            mbAddsiswaSimpan.setOnClickListener {
                val namaLengkap = tietAddsiswaNamalengkap.text.toString()
                val nisn = tietAddsiswaNisn.text.toString()
                val tempatLahir = tietAddsiswaTempatlahir.text.toString()
                val tanggallahir = tietAddsiswaTanggalLahir.text.toString()
                val gender = tietAddsiswaGender.text.toString()
                val religion = tietAddsiswaReligion.text.toString()
                val eduction = tietAddsiswaEducation.text.toString()
                val address = tietAddsiswaAddress.text.toString()
                val namaAyah = tietAddsiswaNamaAyah.text.toString()
                val namaIbu = tietAddsiswaNamaIbu.text.toString()
                val pekerjaanAyah = tietAddsiswaPekerjaanAyah.text.toString()
                val pekerjaanIbu = tietAddsiswaPekerjaanIbu.text.toString()
                val namaWali = tietAddsiswaNamaWali.text.toString()
                val pekerjaanWali = tietAddsiswaPekerjaanWali.text.toString()
                val alamatWali = tietAddsiswaAlamatWali.text.toString()
                val hp = tietAddsiswaHp.text.toString()

                if (namaLengkap.isEmpty()) {
                    tilAddsiswaNamalengkap.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (nisn.isEmpty()) {
                    tietAddsiswaNisn.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (tempatLahir.isEmpty()) {
                    tietAddsiswaTempatlahir.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (tanggallahir.isEmpty()) {
                    tietAddsiswaTanggalLahir.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (gender.isEmpty()) {
                    tietAddsiswaGender.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (religion.isEmpty()) {
                    tietAddsiswaReligion.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (eduction.isEmpty()) {
                    tietAddsiswaEducation.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (address.isEmpty()) {
                    tietAddsiswaAddress.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (namaAyah.isEmpty()) {
                    tietAddsiswaNamaAyah.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (namaIbu.isEmpty()) {
                    tietAddsiswaNamaIbu.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (pekerjaanAyah.isEmpty()) {
                    tietAddsiswaPekerjaanAyah.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (pekerjaanIbu.isEmpty()) {
                    tietAddsiswaPekerjaanIbu.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (namaWali.isEmpty()) {
                    tietAddsiswaNamaWali.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (pekerjaanWali.isEmpty()) {
                    tietAddsiswaPekerjaanWali.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (alamatWali.isEmpty()) {
                    tietAddsiswaAlamatWali.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (hp.isEmpty()) {
                    tietAddsiswaHp.error = resources.getString(R.string.tiet_empty)
                    isValid = false
                }

                if (namaLengkap.isNotEmpty() && nisn.isNotEmpty() && tempatLahir.isNotEmpty() && tanggallahir.isNotEmpty() && gender.isNotEmpty() && religion.isNotEmpty() && eduction.isNotEmpty() && address.isNotEmpty() && namaAyah.isNotEmpty() && namaIbu.isNotEmpty() && pekerjaanAyah.isNotEmpty() && pekerjaanIbu.isNotEmpty() && namaWali.isNotEmpty() && pekerjaanWali.isNotEmpty() && alamatWali.isNotEmpty() && hp.isNotEmpty()) {
                    isValid = true
                }


                if (isValid) {
                    val siswa = Siswa(
                        null,
                        address,
                        eduction,
                        gender,
                        pekerjaanWali,
                        tanggallahir,
                        alamatWali,
                        religion,
                        namaAyah,
                        null,
                        namaWali,
                        namaIbu,
                        hp,
                        namaLengkap,
                        nisn,
                        tempatLahir,
                        pekerjaanAyah,
                        pekerjaanIbu
                    )

                    val bundle = bundleOf()
                    bundle.putParcelable(Constanta.PARCELABLE_ITEM, siswa)
                    bundle.putString(Constanta.PATH_IMAGE, pathImage)
                    mainNavController.navigate(
                        R.id.action_addSiswaFragment_to_addOrangTuaFragment,
                        bundle
                    )
                }
            }
        }
    }

    fun viewAsOrangtua() {
        with(binding) {
            mtvAddsiswaName.text = "Profile Siswa"

            mbAddsiswaSimpan.hide()
            tilAddsiswaAddress.isEnabled = false
            tilAddsiswaAlamatWali.isEnabled = false
            tilAddsiswaEducation.isEnabled = false
            tilAddsiswaGender.isEnabled = false
            tilAddsiswaHp.isEnabled = false
            tilAddsiswaKelas.isEnabled = false
            tilAddsiswaNamaAyah.isEnabled = false
            tilAddsiswaNamaIbu.isEnabled = false
            tilAddsiswaNamaWali.isEnabled = false
            tilAddsiswaNamalengkap.isEnabled = false
            tilAddsiswaNisn.isEnabled = false
            tilAddsiswaPekerjaanAyah.isEnabled = false
            tilAddsiswaPekerjaanIbu.isEnabled = false
            tilAddsiswaPekerjaanWali.isEnabled = false
            tilAddsiswaReligion.isEnabled = false
            tilAddsiswaTanggalLahir.isEnabled = false
            tilAddsiswaTempatlahir.isEnabled = false
            ivAddsiswaPhoto.isEnabled = false
        }
    }
}