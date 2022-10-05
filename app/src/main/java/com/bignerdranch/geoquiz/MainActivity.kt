package com.bignerdranch.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

//1.애플리케이션이 실행되는 동안 속성값을 계속 보존해야할 때 2.애플리케이션 전체에서 사용하는 상수를 정의할 때
//최상위 수준 속성
private const val TAG="MainActivity"
private const val KEY_INDEX="index"

class MainActivity : AppCompatActivity() {

    private val quizViewModel:QuizViewModel by lazy{
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        // ?: 의 경우 왼쪽이 null값이면 오른쪽 값이 나옴(default설정)
        val currentIndex=savedInstanceState?.getInt(KEY_INDEX,0)?:0
        quizViewModel.currentIndex=currentIndex


        trueButton=findViewById(R.id.true_button)
        falseButton=findViewById(R.id.false_button)
        nextButton=findViewById(R.id.next_button)
        questionTextView=findViewById(R.id.question_text_view)

        //단 현재 버전에서는 그냥은 적용 불가 -> android 11(R)이상 적용 X
        trueButton.setOnClickListener{ view: View ->
            checkAnswer(true)
            updateQuestion()
        }
        falseButton.setOnClickListener{view:View ->
            checkAnswer(false)
            updateQuestion()
        }
        nextButton.setOnClickListener {
            if(quizViewModel.checkNext()){
                var score=quizViewModel.getScore()
                Toast.makeText(this,"점수는 $score 점 입니다.",Toast.LENGTH_SHORT).show()
            }
            quizViewModel.moveToNext()
            updateQuestion()
        }
        updateQuestion()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause() called")
    }

    override fun onSaveInstanceState(savedInstancestate: Bundle) {
        super.onSaveInstanceState(savedInstancestate)
        Log.d(TAG,"onSaveInstanceState")
        savedInstancestate.putInt(KEY_INDEX,quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy() called")
    }
    private fun updateQuestion(){
        val questionTextResID=quizViewModel.currentQuestionText
        val questionCorrect=quizViewModel.currentQuestionCorrect
        questionTextView.setText(questionTextResID)
        if(questionCorrect==true){
            trueButton.isEnabled=false
            falseButton.isEnabled=false
        }else{
            trueButton.isEnabled=true
            falseButton.isEnabled=true
        }
    }
    private fun checkAnswer(userAnswer:Boolean){
        val correctAnswer=quizViewModel.currentQuestionAnswer

        val messageResId=if (userAnswer==correctAnswer){
            R.string.correct_toast
        }else{
            R.string.incorrect_toast
        }
        if (userAnswer==correctAnswer){
            quizViewModel.answered()
            updateQuestion()
            quizViewModel.upCorrect()
        }else{
            quizViewModel.upWrong()
        }
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show()
    }
}