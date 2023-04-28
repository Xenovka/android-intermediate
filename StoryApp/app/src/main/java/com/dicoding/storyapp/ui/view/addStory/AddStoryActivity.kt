package com.dicoding.storyapp.ui.view.addStory

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityAddStoryBinding
import com.dicoding.storyapp.model.UserPreference
import com.dicoding.storyapp.reduceFileImage
import com.dicoding.storyapp.rotateFile
import com.dicoding.storyapp.ui.view.ViewModelFactory
import com.dicoding.storyapp.ui.view.camera.CameraActivity
import com.dicoding.storyapp.ui.view.main.MainActivity
import com.dicoding.storyapp.uriToFile
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: AddStoryViewModel
    private var getFile: File? = null
    private var token: String? = null

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.ivPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                binding.ivPreview.setImageURI(uri)
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        supportActionBar?.title = "New Story"

        setupViewModel()

        binding.apply {
            btnCamera.setOnClickListener { startCamera() }
            btnGallery.setOnClickListener { startGallery() }
            btnAdd.setOnClickListener {
                val description = edAddDescription.text.toString()
                if(getFile != null) {
                    val file = reduceFileImage(getFile as File)
                    Toast.makeText(this@AddStoryActivity, "Upload Successful", Toast.LENGTH_SHORT).show()
                    if(description.isNotEmpty()) {
                        showLoading(true)
                        viewModel.uploadStory(file, description, token)
                    } else {
                        edAddDescription.error = "Description can not be empty!"
                        showLoading(false)
                    }
                } else {
                    Toast.makeText(this@AddStoryActivity, "Please take a picture or choose from gallery!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[AddStoryViewModel::class.java]

        viewModel.isSuccessful.observe(this) {
            if(it) {
                showLoading(false)
                switchActivity()
            }
        }

        viewModel.getUser().observe(this) {
            if(it.token?.isNotEmpty() == true) {
                token = it.token
            }
        }
    }

    private fun showLoading(state: Boolean) { binding.progressUpload.visibility = if (state) View.VISIBLE else View.GONE }

    private fun switchActivity() {
        startActivity(Intent(this@AddStoryActivity, MainActivity::class.java))
        finish()
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherCamera.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_image))
        launcherIntentGallery.launch(chooser)
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}