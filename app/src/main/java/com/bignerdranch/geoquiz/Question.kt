package com.bignerdranch.geoquiz

import androidx.annotation.StringRes

data class Question (@StringRes val textResId: Int, val answer:Boolean, var correct:Boolean, var isCheat:Boolean)