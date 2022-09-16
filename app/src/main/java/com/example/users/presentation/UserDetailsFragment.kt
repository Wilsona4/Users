package com.example.users.presentation

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.users.R
import com.example.users.databinding.FragmentUserDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class UserDetailsFragment : Fragment() {
    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()
    private val args: UserDetailsFragmentArgs by navArgs()
    private val tempImageUri by lazy { initTempUri() }

    private val takeImageResultContract =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                tempImageUri.let { uri ->
                    binding.ivProfilePic.load(uri)
                    viewModel.saveImage(args.user.id, uri)
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = args.user

        val address = "${args.address?.street} ${args.address?.suite}"
        binding.apply {
            tvName.text = args.name
            tvUsername.text = args.username
            tvEmail.text = args.email
            tvPhone.text = args.phone
            tvWebsite.text = args.website
            tvCompany.text = args.company.name
            tvAddress.text = address
            ivProfilePic.load(args.imageUri) {
                placeholder(R.drawable.image_32)
                error(R.drawable.image_32)
            }
        }

        binding.ivProfilePic.setOnClickListener {
            takeImageResultContract.launch(tempImageUri)
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}