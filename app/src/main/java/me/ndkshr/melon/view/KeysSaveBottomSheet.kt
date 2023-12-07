package me.ndkshr.melon.view

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.ndkshr.melon.R
import me.ndkshr.melon.databinding.FragmentApiKeysResetBinding
import me.ndkshr.melon.worker.getGooeyAuthKey
import me.ndkshr.melon.worker.getOpenAIAuthKey
import me.ndkshr.melon.worker.saveGooeyItems
import me.ndkshr.melon.worker.saveOpenAiItems

const val OPEN_AI_KEY_PREF = "openaikeypref"
const val GOOEY_KEY_PREF = "gooeykeypref"

class KeysSaveBottomSheet: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentApiKeysResetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_api_keys_reset, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gooeyEditText.setText(getGooeyAuthKey(requireActivity()))


        binding.saveGooeyKey.setOnClickListener {
            val authKey = binding.gooeyEditText.text.toString()

            if (authKey.isNotEmpty()) {
                saveGooeyItems(requireActivity(), authKey)
            }
        }

        binding.openaiEditText.setText(getOpenAIAuthKey(requireActivity()))

        binding.saveOpenaiKey.setOnClickListener {
            val authKey = binding.openaiEditText.text.toString()
            if (authKey.isNotEmpty()) {
                saveOpenAiItems(requireActivity(), authKey)
            }
        }

    }
}