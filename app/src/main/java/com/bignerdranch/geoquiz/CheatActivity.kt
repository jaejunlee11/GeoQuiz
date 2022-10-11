package com.bignerdranch.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

const val EXTRA_ANSWER_SHOWN="com.bignerdranch.android.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE="com.bignerdranch.android.geoquiz.answer_is_true"
class CheatActivity : AppCompatActivity() {


    private val quizViewModel:QuizViewModel by lazy{
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }
    private lateinit var answerTextView:TextView
    private lateinit var showAnswerButton:Button

    private var answerIsTrue=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        //intent로 받았던 값을 꺼내서 담음
        answerIsTrue=intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false)
        answerTextView=findViewById(R.id.answer_text_view)
        showAnswerButton=findViewById(R.id.show_answer_button)
        showAnswerButton.setOnClickListener {
            val answerText=when{
                answerIsTrue->R.string.true_button
                else->R.string.false_button
            }
            answerTextView.setText(answerText)
            quizViewModel.isCheater=true
            setAnswerShownResult(quizViewModel.isCheater)
        }
        update()
    }
    //mainActivity에 값을 넘겨주기 위해서 만든 함수
    private fun setAnswerShownResult(isAnswerShown:Boolean){
        //넘겨줄 intnt에 정답을 열어봤는 지 여부를 담음
        val data = Intent().apply{
            putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown)
        }
        //담은 intent를 결과를 결과에 세팅해줌
        setResult(Activity.RESULT_OK,data)
    }
    //mainActivity에서 값을 받아오기 위해서 만들어 놓은 함수
    companion object{
        fun newIntent(packageContext: Context,answerIsTrue:Boolean): Intent {
            return Intent(packageContext,CheatActivity::class.java).apply{
                putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue)
            }
        }
    }
    fun update() {
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        if (quizViewModel.isCheater == true) {
            answerTextView.setText(answerText)
            setAnswerShownResult(quizViewModel.isCheater)
        }
    }
}