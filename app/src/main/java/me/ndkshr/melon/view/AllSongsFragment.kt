package me.ndkshr.melon.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import me.ndkshr.melon.ViewModelFactory
import me.ndkshr.melon.databinding.FragmentAllSongsBinding
import me.ndkshr.melon.utils.gone
import me.ndkshr.melon.utils.visible
import me.ndkshr.melon.viewmodel.MainActivityViewModel
import me.ndkshr.melon.worker.FetchSongsWorker

private const val TAG = "AllSongsFragment"
class AllSongsFragment: Fragment() {

    companion object {
        const val STORAGE_REQUEST_CODE = 111
    }

    fun show(fm: FragmentManager, containerId: Int) {
        val ft = fm.beginTransaction()
        ft.replace(containerId, this, TAG)
        ft.commit()
    }

    private val viewModelFactory = ViewModelFactory()
    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainActivityViewModel::class.java)
    }

    private lateinit var songsAdapter: SongsAdapter

    private lateinit var binding: FragmentAllSongsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllSongsBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        songsAdapter = SongsAdapter(viewModel, ::startPlayerWithAudio)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requestReadPermission()
        binding.fetchSongsButton.setOnClickListener {
            requestReadPermission()
        }
        if (viewModel.songsList.isEmpty()) binding.loaderProgress.visible()
    }

    fun fetchAndSetSongs(){
        viewModel.songsList = FetchSongsWorker(requireActivity()).fetchSongs()
        if (viewModel.songsList.isNotEmpty()) {
            binding.loaderProgress.gone()
        }
        binding.songsList.apply {
            adapter = songsAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
        showRequestPermissionMessage(false)
    }

    fun showRequestPermissionMessage(shouldShow: Boolean) {
        binding.fetchSongsButton.isVisible = shouldShow
        binding.permissionMsg.isVisible = shouldShow
        binding.songsList.isVisible = !shouldShow
    }

    private fun requestReadPermission() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE)
        } else {
            fetchAndSetSongs()
        }
    }

    private fun startPlayerWithAudio(position: Int) {
        viewModel.currentSongPosition.postValue(position)
        PlayerBottomSheetFragment.playCurrentSong(activity?.supportFragmentManager)
//        DefaultPlayerBottomSheet.show(audio, activity?.supportFragmentManager)

//        binding.miniPlayer.setOnClickListener {
//            // TODO: Swipe to remove, normal click should open the detail player view
//            showMiniPlayer(false)
//        }

        showMiniPlayer(false)
    }

    private fun showMiniPlayer(show: Boolean) {
        if (show) {
            binding.miniPlayer.visible()
        } else {
            binding.miniPlayer.gone()
        }
    }

}
