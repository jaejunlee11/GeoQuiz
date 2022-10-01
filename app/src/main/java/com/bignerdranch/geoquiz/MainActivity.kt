package com.bignerdranch.geoquiz

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton:Button

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

        //단 현재 버전에서는 그냥은 적용 불가 -> android 11(R)이상 적용 X
        trueButton.setOnClickListener{ view: View ->
            Toast.makeText(this,R.string.correct_tost,Toast.LENGTH_SHORT).run{
                this.setGravity(Gravity.TOP,0,0)
                this.show()}
        }
        falseButton.setOnClickListener{view:View ->
            Toast.makeText(this,R.string.incorrect_toast,Toast.LENGTH_SHORT).run{
                this.setGravity(Gravity.TOP,0,0)
                this.show()
            }
        }
    }
}