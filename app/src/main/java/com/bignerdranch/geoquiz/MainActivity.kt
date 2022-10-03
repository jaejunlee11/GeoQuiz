package com.bignerdranch.geoquiz

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton:Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton:ImageButton
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia,true),
        Question(R.string.question_oceans,true),
        Question(R.string.question_mideast,false),
        Question(R.string.question_africa,false),
        Question(R.string.question_americas,true),
        Question(R.string.question_asia,true))

    private var currentIndex=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton=findViewById(R.id.true_button)
        falseButton=findViewById(R.id.false_button)
        nextButton=findViewById(R.id.next_button)
        previousButton=findViewById(R.id.previous_button)
        questionTextView=findViewById(R.id.question_text_view)

        //단 현재 버전에서는 그냥은 적용 불가 -> android 11(R)이상 적용 X
        trueButton.setOnClickListener{ view: View ->
            checkAnswer(true)
        }
        falseButton.setOnClickListener{view:View ->
            checkAnswer(false)
        }
        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            val questionTextResId = questionBank[currentIndex].textResId
            updateQuestion()
        }
        previousButton.setOnClickListener {
            if (currentIndex==0){
                currentIndex+=questionBank.size
            }
            currentIndex=(currentIndex-1)%questionBank.size
            val questionTextResId=questionBank[currentIndex].textResId
            updateQuestion()
        }
        questionTextView.setOnClickListener{view:View->
            currentIndex=(currentIndex+1)%questionBank.size
            val questionTextResId=questionBank[currentIndex].textResId
            updateQuestion()
        }
        updateQuestion()

    }
    private fun updateQuestion(){
        val questionTextResID=questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResID)
    }
    private fun checkAnswer(userAnswer:Boolean){
        val correctAnswer=questionBank[currentIndex].answer

        val messageResId=if (userAnswer==correctAnswer){
            R.string.correct_toast
        }else{
            R.string.incorrect_toast
        }
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show()
    }
}