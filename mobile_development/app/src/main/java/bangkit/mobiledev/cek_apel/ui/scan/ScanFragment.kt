package bangkit.mobiledev.cek_apel.ui.scan

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bangkit.mobiledev.cek_apel.databinding.FragmentScanBinding
import bangkit.mobiledev.cek_apel.utils.getImageUri
import bangkit.mobiledev.cek_apel.utils.reduceFileImage
import bangkit.mobiledev.cek_apel.utils.uriToFile

class ScanFragment : Fragment() {

    private lateinit var binding: FragmentScanBinding
    private val scanViewModel: ScanViewModel by viewModels()
    private var currentImageUri: Uri? = null

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            processAndShowImage(uri)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri?.let { processAndShowImage(it) }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(requireContext(), "Camera permission required", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanBinding.inflate(inflater, container, false)

        currentImageUri = savedInstanceState?.getParcelable(STATE_IMAGE_URI)

        if (currentImageUri != null) {
            showImage()
        }

        checkAndRequestPermission()

        binding.btnGaleri.setOnClickListener { startGallery() }
        binding.btnKamera.setOnClickListener { startCamera() }

        return binding.root
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        if (currentImageUri == null) {
            Toast.makeText(requireContext(), "Failed to get photo URI", Toast.LENGTH_SHORT).show()
            return
        }
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private fun processAndShowImage(uri: Uri) {
        val imageFile = uriToFile(uri, requireContext())

        val reducedFile = imageFile.reduceFileImage()

        binding.placeholderImage.setImageURI(Uri.fromFile(reducedFile))
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.placeholderImage.setImageURI(it)
        } ?: run {
            Toast.makeText(requireContext(), "Image not found", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun checkAndRequestPermission() {
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(requireContext(), REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STATE_IMAGE_URI, currentImageUri)
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        private const val STATE_IMAGE_URI = "state_image_uri"
    }
}

