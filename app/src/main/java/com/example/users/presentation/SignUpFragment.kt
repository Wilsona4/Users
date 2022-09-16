package com.example.users.presentation

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.users.R
import com.example.users.data.model.Address
import com.example.users.data.model.Company
import com.example.users.data.model.User
import com.example.users.databinding.FragmentSignUpBinding
import com.example.users.util.ValidationObject
import com.example.users.util.validateEditText
import java.io.File

class SignUpFragment : Fragment() {


    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()

    private val tempImageUri by lazy { initTempUri() }

    private var imageUri: Uri? = null

    private val takeImageResultContract =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                tempImageUri.let { uri ->
                    imageUri = uri
                    binding.ivBackdrop.load(uri) {
                        placeholder(R.drawable.image_32)
                        error(R.drawable.image_32)
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validateEmail()
        binding.apply {
            tvFullname.validateEditText(binding.tilFullname)
            tvUsername.validateEditText(binding.tilUsername)
            tvPhone.validateEditText(binding.tilPhone)
            tvWebsite.validateEditText(binding.tilWebsite)
            tvCompany.validateEditText(binding.tilCompany)
            tvAddress.validateEditText(binding.tilAddress)
        }

        binding.fabCamera.setOnClickListener {
            takeImageResultContract.launch(tempImageUri)
        }
        binding.ivBackdrop.setOnClickListener {
            takeImageResultContract.launch(tempImageUri)
        }

        binding.btSubmit.setOnClickListener {

            val name = binding.tvFullname.text.toString().trim()
            val userName = binding.tvUsername.text.toString().trim()
            val email = binding.tvEmail.text.toString().trim()
            val phone = binding.tvPhone.text.toString().trim()
            val website = binding.tvWebsite.text.toString().trim()
            val company = binding.tvCompany.text.toString().trim()
            val address = binding.tvAddress.text.toString().trim()
            val myImageUri = imageUri

            when {
                myImageUri == null -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.kindly_take_your_picture),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener

                }
                name.isEmpty() -> {
                    binding.tilFullname.error = getString(R.string.cant_be_empty)
                    return@setOnClickListener
                }
                userName.isEmpty() -> {
                    binding.tilUsername.error = getString(R.string.cant_be_empty)
                    return@setOnClickListener
                }
                phone.isEmpty() -> {
                    binding.tilPhone.error = getString(R.string.cant_be_empty)
                    return@setOnClickListener
                }
                email.isEmpty() -> {
                    binding.tilEmail.error = getString(R.string.cant_be_empty)
                    return@setOnClickListener
                }
                address.isEmpty() -> {
                    binding.tilAddress.error = getString(R.string.cant_be_empty)
                    return@setOnClickListener
                }
                company.isEmpty() -> {
                    binding.tilCompany.error = getString(R.string.cant_be_empty)
                    return@setOnClickListener
                }
                website.isEmpty() -> {
                    binding.tilWebsite.error = getString(R.string.cant_be_empty)
                    return@setOnClickListener
                }
                !ValidationObject.validateEmail(email) -> {
                    binding.tilEmail.error =
                        getString(R.string.invalid_email)
                    return@setOnClickListener
                }
                else -> {
                    val user = User(
                        name = name,
                        username = userName,
                        email = email,
                        phone = phone,
                        website = website,
                        imageUri = imageUri.toString(),
                        company = Company(name = company, bs = "", catchPhrase = ""),
                        address = Address(city = "", street = address, suite = "", zipcode = "")
                    )
                    viewModel.addUser(user)
                    findNavController().popBackStack()
                }
            }
        }
    }


    /*Function to Validate Email as Input Changes*/
    private fun validateEmail() {

        binding.tvEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (ValidationObject.validateEmail(binding.tvEmail.text.toString().trim())) {
                    binding.tilEmail.error = null
                    binding.tilEmail.isErrorEnabled = false
                } else {
                    binding.tilEmail.error = getString(R.string.invalid_email)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (ValidationObject.validateEmail(binding.tvEmail.text.toString().trim())) {
                    binding.tilEmail.error = null
                    binding.tilEmail.isErrorEnabled = false
                } else {
                    binding.tilEmail.error = getString(R.string.invalid_email)
                }
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initTempUri(): Uri {
        //gets the temp_images dir
        val tempImagesDir = File(
            requireContext().filesDir, //this function gets the external cache dir
            getString(R.string.temp_images_dir)
        ) //gets the directory for the temporary images dir

        tempImagesDir.mkdir() //Create the temp_images dir

        //Creates the temp_image.jpg file
        val tempImage = File(
            tempImagesDir, //prefix the new abstract path with the temporary images dir path
            getString(R.string.temp_image)
        ) //gets the abstract temp_image file name

        //Returns the Uri object to be used with ActivityResultLauncher
        return FileProvider.getUriForFile(
            requireContext(),
            getString(R.string.authorities),
            tempImage
        )
    }
}