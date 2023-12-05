package me.ndkshr.melon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.ndkshr.melon.R
import me.ndkshr.melon.ViewModelFactory
import me.ndkshr.melon.databinding.FragmentGenerateSongBottomSheetBinding
import me.ndkshr.melon.utils.gone
import me.ndkshr.melon.utils.invisible
import me.ndkshr.melon.utils.visible
import me.ndkshr.melon.viewmodel.MainActivityViewModel

class GenerateSongBottomSheet: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentGenerateSongBottomSheetBinding

    private var selected: LyricModelType = LyricModelType.NO_LYRIC

    private val viewModelFactory = ViewModelFactory()
    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_generate_song_bottom_sheet, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentGeneratedLyrics.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.loadingTv.text = "Generating music..."
                viewModel.generateSongFromGooey(
                    binding.titleInput.text.toString(),
                    binding.promptInput.text.toString(),
                    it
                )
            }
        }

        binding.generateButton.setOnClickListener {
            if (binding.promptInput.text?.isNotEmpty() == true && binding.titleInput.text?.isNotEmpty() == true) {
                var lyrics = ""
                if (selected == LyricModelType.WITH_LYRIC) {
                    lyrics = "happy birthday to you,#happy birthday to you#happy birthday to Nandu"
                    binding.loadingTv.text = "Generating lyrics..."
                    viewModel.generateLyricsFromOpenAi(binding.titleInput.text.toString())
                } else {
                    binding.loadingTv.text = "Generating music..."
                    viewModel.generateSongFromGooey(
                        binding.titleInput.text.toString(),
                        binding.promptInput.text.toString(),
                        lyrics
                    )
                }

                binding.generateButton.setBackgroundColor(requireActivity().getColor(R.color.grey))
                binding.generateButton.setTextColor(requireActivity().getColor(R.color.dar_grey))
                binding.titleInputLayout.invisible()
                binding.textInputLayout.invisible()
                binding.generateButton.invisible()
                binding.withLyrics.invisible()
                binding.noLyrics.invisible()
                binding.progress.visible()
                binding.loadingTv.visible()
            } else {
                Toast.makeText(requireActivity(), "Fill All Forms", Toast.LENGTH_SHORT).show()
            }
        }

        binding.noLyrics.setOnClickListener {
            selected = LyricModelType.NO_LYRIC
            binding.withLyrics.setBackgroundColor(requireActivity().getColor(R.color.dar_grey))
            binding.withLyrics.setTextColor(requireActivity().getColor(R.color.mint))
            binding.noLyrics.setTextColor(requireActivity().getColor(R.color.white))
            binding.noLyrics.setBackgroundColor(requireActivity().getColor(R.color.mint))
        }

        binding.withLyrics.setOnClickListener {
            selected = LyricModelType.WITH_LYRIC
            binding.noLyrics.setBackgroundColor(requireActivity().getColor(R.color.dar_grey))
            binding.noLyrics.setTextColor(requireActivity().getColor(R.color.mint))
            binding.withLyrics.setTextColor(requireActivity().getColor(R.color.white))
            binding.withLyrics.setBackgroundColor(requireActivity().getColor(R.color.mint))
        }
    }

    enum class LyricModelType {
        WITH_LYRIC, NO_LYRIC
    }
}