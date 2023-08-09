package com.wheel.game.ui.main_menu

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.wheel.game.R
import com.wheel.game.databinding.FragmentMainMenuBinding
import com.wheel.game.ui.other.ViewBindingFragment

class FragmentMainMenu : ViewBindingFragment<FragmentMainMenuBinding>(FragmentMainMenuBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.exit.setOnClickListener {
            requireActivity().finish()
        }
        binding.play.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentWheel)
        }
        binding.privacyText.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    ACTION_VIEW,
                    Uri.parse("https://www.google.com")
                )
            )
        }
    }
}