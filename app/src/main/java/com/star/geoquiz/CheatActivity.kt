package com.star.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cheat.*

private const val EXTRA_ANSWER_IS_TRUE = "com.star.geoquiz.answer_is_true"
private const val EXTRA_ANSWER_SHOWN = "com.star.geoquiz.answer_shown"

class CheatActivity : AppCompatActivity() {

    private val cheatViewModel: CheatViewModel by viewModels()

    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        show_answer_button.setOnClickListener {
            cheatViewModel.updateCheatAnswer(answerIsTrue)
            getShownAnswer()
        }

        getShownAnswer()
    }

    private fun getShownAnswer() {

        updateShownAnswer()
        updateButton()
        updateAPILevel()
        setAnswerShowResult(cheatViewModel.answerIsShown)
    }

    private fun updateShownAnswer() {

        answer_text_view.setText(cheatViewModel.cheatAnswer)
    }

    private fun updateButton() {

        show_answer_button.isEnabled = !cheatViewModel.answerIsShown
    }

    private fun updateAPILevel() {

        api_level.text = getString(R.string.api_level, Build.VERSION.SDK_INT)
    }

    private fun setAnswerShowResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }

        fun wasAnswerShown(data: Intent?): Boolean {
            return data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

}
