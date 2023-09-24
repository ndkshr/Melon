package me.ndkshr.melon.view

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import me.ndkshr.melon.R
import me.ndkshr.melon.ViewModelFactory
import me.ndkshr.melon.view.AllSongsFragment.Companion.STORAGE_REQUEST_CODE
import me.ndkshr.melon.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private val viewModelFactory = ViewModelFactory()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
    }

    private lateinit var fragmentContainer: FrameLayout

    private lateinit var fragmentSongs: AllSongsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setUpSongsFragment()
    }

    private fun initViews() {
        fragmentContainer = findViewById(R.id.fragment_container)
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
}