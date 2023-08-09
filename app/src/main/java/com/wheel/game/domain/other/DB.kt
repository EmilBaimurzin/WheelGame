package com.wheel.game.domain.other

import android.content.Context

class DB(private val context: Context) {
    private val sp = context.getSharedPreferences("SP", Context.MODE_PRIVATE)

    fun getBalance(): Int = sp.getInt("BALANCE", 1500)

    fun setBalance(value: Int) = sp.edit().putInt("BALANCE", getBalance() + value).apply()
}