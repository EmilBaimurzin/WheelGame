package com.wheel.game.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wheel.game.R
import com.wheel.game.core.library.ViewBindingDialog
import com.wheel.game.databinding.DialogFinishesBinding

class DialogFinishes: ViewBindingDialog<DialogFinishesBinding>(DialogFinishesBinding::inflate) {
    private val args: DialogFinishesArgs by navArgs()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Dialog_No_Border)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setCancelable(false)
        dialog!!.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                findNavController().popBackStack(R.id.fragmentMain, false, false)
                true
            } else {
                false
            }
        }
        binding.money.text = args.money.toString()
        binding.container.setBackgroundResource(if (args.money < 0) R.drawable.loss else R.drawable.victory)

        binding.buttonClose.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
        }

        binding.buttonNext.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentWheel)
        }
    }
}