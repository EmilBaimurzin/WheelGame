package com.wheel.game.ui.wheel

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wheel.game.R
import com.wheel.game.databinding.FragmentWheelBinding
import com.wheel.game.domain.game.WheelState
import com.wheel.game.domain.other.DB
import com.wheel.game.ui.other.ViewBindingFragment

class FragmentWheel : ViewBindingFragment<FragmentWheelBinding>(FragmentWheelBinding::inflate) {
    private val sp by lazy {
        DB(requireContext())
    }
    private val viewModel: WheelViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBalance()
        binding.spinButton.setOnClickListener {
            if (viewModel.gameState.value!! == WheelState.PRE_SPIN) {
                if (sp.getBalance() >= 300) {
                    sp.setBalance(-300)
                    setBalance()
                    viewModel.spin()
                }
            }
        }

        binding.redCard.setOnClickListener {
            if (viewModel.cards.value == null && viewModel.gameState.value == WheelState.CHOICE) {
                viewModel.setCards(true)
            }
        }

        binding.grayCard.setOnClickListener {
            if (viewModel.cards.value == null && viewModel.gameState.value == WheelState.CHOICE) {
                viewModel.setCards(false)
            }
        }

        binding.meuButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.collectButton.setOnClickListener {
            if (viewModel.gameState.value == WheelState.CHOICE && viewModel.cards.value == null) {
                sp.setBalance(viewModel.win)
                setBalance()
                findNavController().navigate(FragmentWheelDirections.actionFragmentWheelToDialogFinishes(viewModel.win))
            }
        }

        viewModel.winCallback = {
            setBalance()
            if (it > 0) {
                sp.setBalance(it)
                setBalance()
                findNavController().navigate(FragmentWheelDirections.actionFragmentWheelToDialogFinishes(it))
            } else {
                findNavController().navigate(FragmentWheelDirections.actionFragmentWheelToDialogFinishes(it))
            }
        }

        viewModel.rotation.observe(viewLifecycleOwner) {
            binding.wheel.rotation = -it + 100
        }

        viewModel.cards.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    null -> {
                        grayCard.setImageResource(R.drawable.black)
                        redCard.setImageResource(R.drawable.red)
                        grayCard.isVisible = true
                        redCard.isVisible = true
                    }

                    true -> {
                        grayCard.setImageResource(R.drawable.black)
                        redCard.setImageResource(R.drawable.red01)
                        grayCard.isVisible = false
                        redCard.isVisible = true
                    }

                    false -> {
                        grayCard.setImageResource(R.drawable.black01)
                        redCard.setImageResource(R.drawable.red)
                        grayCard.isVisible = true
                        redCard.isVisible = false
                    }
                }
            }
        }

        viewModel.gameState.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    WheelState.PRE_SPIN -> {
                        spinButton.isVisible = true
                        collectButton.isVisible = false
                    }

                    WheelState.SPIN -> {
                        spinButton.isVisible = false
                        collectButton.isVisible = false
                    }

                    WheelState.CHOICE -> {
                        spinButton.isVisible = false
                        collectButton.isVisible = true
                    }

                    else -> {
                        spinButton.isVisible = false
                        collectButton.isVisible = false
                    }
                }
            }
        }
    }

    private fun setBalance() {
        binding.balance.text = sp.getBalance().toString()
    }
}