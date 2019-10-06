package com.star.geoquiz

import androidx.lifecycle.ViewModel

class CheatViewModel : ViewModel(){

    var answerIsShown = false
    var cheatAnswer = R.string.cheat_answer_not_shown

    fun updateCheatAnswer(answerIsTrue: Boolean) {

        answerIsShown = true
        cheatAnswer = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
    }
}
