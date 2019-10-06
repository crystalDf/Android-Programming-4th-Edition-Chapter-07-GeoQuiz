package com.star.geoquiz

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean,
                    var answered: Boolean = false, var correct: Boolean = false,
                    var isCheater: Boolean = false)
