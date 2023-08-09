package com.wheel.game.ui.wheel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wheel.game.core.library.random
import com.wheel.game.domain.game.WheelState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class WheelViewModel : ViewModel() {
    var win = 0
    var winCallback: ((Int) -> Unit)? = null

    private val _gameState = MutableLiveData(WheelState.PRE_SPIN)
    val gameState: LiveData<WheelState> = _gameState

    private val _cards = MutableLiveData<Boolean?>(null)
    val cards: LiveData<Boolean?> = _cards

    private val _rotation = MutableLiveData(0f)
    val rotation: LiveData<Float> = _rotation

    fun spin() {
        viewModelScope.launch {
            _gameState.postValue(WheelState.SPIN)
            var value = (8..25).random().toFloat()
            val randomDelay = 10 random 13
            val randomDecrease = Random.nextFloat() * (0.044f - 0.067f) + 0.084f
            while (value > 0.10) {
                delay(randomDelay.toLong())
                if (value > 0.10f) {
                    value -= randomDecrease
                }
                if (value - randomDecrease < 0.10f) {
                    val values = listOf(
                        600, 1000, 400, 200, 800, 300, 100, 500,
                        700, 500, 200, 800, 100, 300, 500, 100
                    )
                    val totalSections = values.size
                    val degreesPerSection = 360f / totalSections
                    val normalizedDegrees = (_rotation.value!!) % 360f

                    val sectionIndex = normalizedDegrees / degreesPerSection
                    if (_gameState.value!! == WheelState.SPIN) {
                        _gameState.postValue(WheelState.CHOICE)
                        win = values[sectionIndex.toInt()]
                    }
                }
                _rotation.postValue(_rotation.value!! + value)
            }
        }
    }

    fun setCards(isRed: Boolean) {
        viewModelScope.launch {
            _gameState.postValue(WheelState.END)
            _cards.postValue(Random.nextBoolean())
            delay(10)
            val isWin = _cards.value!! && isRed || !_cards.value!! && !isRed
            val reward = if (isWin) win * 2 else -300
            delay(1400)
            winCallback?.invoke(reward)
        }
    }
}