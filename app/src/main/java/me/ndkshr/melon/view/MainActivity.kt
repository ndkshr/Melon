package me.ndkshr.melon.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import me.ndkshr.melon.R
import me.ndkshr.melon.ViewModelFactory
import me.ndkshr.melon.databinding.ActivityMainBinding
import me.ndkshr.melon.utils.PreferenceUtils
import me.ndkshr.melon.view.AllSongsFragment.Companion.STORAGE_REQUEST_CODE
import me.ndkshr.melon.viewmodel.MainActivityViewModel
import me.ndkshr.melon.worker.GooeySongDownloadManager
import me.ndkshr.melon.worker.LYRIC_DELIM
import me.ndkshr.melon.worker.TITLE_DELIM
import java.util.UUID


class MainActivity : AppCompatActivity() {

    private val viewModelFactory = ViewModelFactory()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var fragmentContainer: FrameLayout

    private lateinit var fragmentSongs: AllSongsFragment

    private lateinit var downloader: GooeySongDownloadManager

    private val generateSongBottomSheet: GenerateSongBottomSheet by lazy {
        GenerateSongBottomSheet()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initViews()

        setUpSongsFragment()

        downloader = GooeySongDownloadManager(this)

        attachLiveData()

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
    }

    private fun initViews() {
        fragmentContainer = findViewById(R.id.fragment_container)

        binding.generateSongButton.setOnClickListener {
            generateSongBottomSheet.show(supportFragmentManager, "GenerateSongFragment")
        }

        binding.appBarParent.setOnClickListener {
            val fragment = KeysSaveBottomSheet()
            fragment.show(supportFragmentManager, "KeysFragment")
        }
    }

    private fun setUpSongsFragment() {
        fragmentSongs = AllSongsFragment()
        fragmentSongs.show(supportFragmentManager, fragmentContainer.id)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_REQUEST_CODE) {
            if (permissions[0] == android.Manifest.permission.READ_EXTERNAL_STORAGE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                fragmentSongs.fetchAndSetSongs()
            } else {
                fragmentSongs.showRequestPermissionMessage(true)
            }
        }
    }

    private fun attachLiveData() {
        viewModel.gooeyResponseLiveData.observe(this) {
            val downloadUrl = it.output.outputAudios.audioLDM[0]
            Toast.makeText(this, "API CALL SUCCESS | Link = $downloadUrl", Toast.LENGTH_SHORT).show()

            val uuid = UUID.randomUUID()
            val lyrics = viewModel.currentGeneratedLyrics.value ?: "".replace("\n", LYRIC_DELIM)

            val title = "Generated$TITLE_DELIM${it.title}$TITLE_DELIM${uuid}"

            downloader.downloadFile(title, downloadUrl)

            if (lyrics.isNotEmpty()) {
                PreferenceUtils.setStringPref(this, uuid.toString(), lyrics)
            }

            generateSongBottomSheet.dismiss()

            Handler(mainLooper).postDelayed({
                fragmentSongs.fetchAndSetSongs()
            }, 5000)

            viewModel.currentGeneratedLyrics.postValue("")
        }
    }
}