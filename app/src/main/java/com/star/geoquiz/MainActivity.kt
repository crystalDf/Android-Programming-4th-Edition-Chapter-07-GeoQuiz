package com.star.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

private const val KEY_CURRENT_INDEX = "currentIndex"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate(Bundle?) called")

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        quizViewModel.currentIndex =
            savedInstanceState?.getInt(KEY_CURRENT_INDEX, 0) ?: 0

        question_text_view.setOnClickListener {
            next_button.performClick()
        }

        true_button.setOnClickListener {
            getAnswer(true)
        }

        false_button.setOnClickListener {
            getAnswer(false)
        }

        prev_button.setOnClickListener {
            getQuestion(QuizViewModel.Order.PREV)
        }

        next_button.setOnClickListener {
            getQuestion(QuizViewModel.Order.NEXT)
        }

        prev_image_button.setOnClickListener {
            prev_button.performClick()
        }

        next_image_button.setOnClickListener {
            next_button.performClick()
        }

        cheat_button.setOnClickListener { view ->
            getCheatAnswer(view)
        }

        getQuestion(QuizViewModel.Order.CURRENT)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(KEY_CURRENT_INDEX, quizViewModel.currentIndex)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_CHEAT && resultCode == Activity.RESULT_OK) {
            quizViewModel.updateIsCheater(CheatActivity.wasAnswerShown(data))

            updateButton()
        }
    }

    override fun onStart() {
        super.onStart()

        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()

        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()

        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy() called")
    }

    private fun getQuestion(order: QuizViewModel.Order) {

        quizViewModel.updateIndex(order)
        updateQuestion()
        updateButton()
    }

    private fun getAnswer(userAnswer: Boolean) {

        quizViewModel.updateAnswer(userAnswer)
        checkAnswer()
        updateButton()
    }

    private fun updateQuestion() {

        val questionTextResId = quizViewModel.currentQuestionTextResId
        question_text_view.setText(questionTextResId)
    }

    private fun updateButton() {

        val currentQuestionAnswered = quizViewModel.currentQuestionAnswered
        val allAnswered = quizViewModel.allAnswered
        val currentQuestionIsCheater = quizViewModel.currentQuestionIsCheater
        val remainingCheatTokens = quizViewModel.remainingCheatTokens

        true_button.isEnabled = !currentQuestionAnswered
        false_button.isEnabled = !currentQuestionAnswered

        cheat_button.isEnabled = ((!currentQuestionAnswered) &&
                (!currentQuestionIsCheater) && (remainingCheatTokens > 0))
        remaining_cheat_tokens_text_view.text =
            getString(R.string.remaining_cheat_tokens, remainingCheatTokens)

        prev_button.isEnabled = !allAnswered
        next_button.isEnabled = !allAnswered
        prev_image_button.isEnabled = !allAnswered
        next_image_button.isEnabled = !allAnswered
    }

    private fun checkAnswer() {

        val messageResId = when {
            quizViewModel.currentQuestionIsCheater -> R.string.judgment_toast
            quizViewModel.currentQuestionCorrect -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        var result = getString(messageResId)

        if (quizViewModel.allAnswered) {
            result += "\n" + "Score: " + String.format("%.2f", quizViewModel.getScore())
        }

        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
    }

    private fun getCheatAnswer(view: View) {
        val answerIsTrue = quizViewModel.currentQuestionAnswer
        val intent = CheatActivity.newIntent(this, answerIsTrue)
        val options = ActivityOptionsCompat
            .makeClipRevealAnimation(view, 0, 0, view.width, view.height)

        startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
    }
}
