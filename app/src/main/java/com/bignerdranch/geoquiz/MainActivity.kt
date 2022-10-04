package com.bignerdranch.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//1.애플리케이션이 실행되는 동안 속성값을 계속 보존해야할 때 2.애플리케이션 전체에서 사용하는 상수를 정의할 때
//최상위 수준 속성
private const val TAG="MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton:Button
    private lateinit var nextButton:Button
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia,true,false),
        Question(R.string.question_oceans,true,false),
        Question(R.string.question_mideast,false,false),
        Question(R.string.question_africa,false,false),
        Question(R.string.question_americas,true,false),
        Question(R.string.question_asia,true,false))

    private var currentIndex=0
    private var wrongAnswer=0
    private var correctANswer=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueButton=findViewById(R.id.true_button)
        falseButton=findViewById(R.id.false_button)
        nextButton=findViewById(R.id.next_button)
        questionTextView=findViewById(R.id.question_text_view)

        //단 현재 버전에서는 그냥은 적용 불가 -> android 11(R)이상 적용 X
        trueButton.setOnClickListener{ view: View ->
            checkAnswer(true)
        }
        falseButton.setOnClickListener{view:View ->
            checkAnswer(false)
            updateQuestion()
        }
        nextButton.setOnClickListener {
            if(currentIndex==questionBank.size-1){
                var score=(100*correctANswer/(correctANswer+wrongAnswer))
                Toast.makeText(this,"점수는 $score 점 입니다.",Toast.LENGTH_SHORT).show()
            }
            currentIndex=(currentIndex+1)%questionBank.size
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

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy() called")
    }
    private fun updateQuestion(){
        val questionTextResID=questionBank[currentIndex].textResId
        val questionCorrect=questionBank[currentIndex].correct
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
        val correctAnswer=questionBank[currentIndex].answer

        val messageResId=if (userAnswer==correctAnswer){
            R.string.correct_toast
        }else{
            R.string.incorrect_toast
        }
        if (userAnswer==correctAnswer){
            questionBank[currentIndex].correct=true
            updateQuestion()
            correctANswer+=1
        }else{
            wrongAnswer+=1
        }
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show()
    }
}