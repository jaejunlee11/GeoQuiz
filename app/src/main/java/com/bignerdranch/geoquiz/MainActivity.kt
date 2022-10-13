package com.bignerdranch.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
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
private const val REQUEST_CODE_CHEAT = 0;

class MainActivity : AppCompatActivity() {

    private val quizViewModel:QuizViewModel by lazy{
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var cheatButton:Button
    private lateinit var questionTextView: TextView

    @SuppressLint("RestrictedApi")
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
        cheatButton=findViewById(R.id.cheat_button)
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
        cheatButton.setOnClickListener {view->
            //넘겨줄 데이터 저장
            val answerIsTrue=quizViewModel.currentQuestionAnswer
            var cheatCount=quizViewModel.cheatCount
            //cheatActivity에 있는 함수 실행-> intent에 정답을 담음
            val intent=CheatActivity.newIntent(this@MainActivity,answerIsTrue,cheatCount)
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                val options=ActivityOptions.makeClipRevealAnimation(view,0,0,view.width,view.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT,options.toBundle())
            }else{
                //엑티비티를 실행할 때 뒤로가기를 통해 오면 결과를 받도록 설정,setResult를 안하고 나오면 Activity.RESULT_CANCELED를 받음
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
        }
        updateQuestion()
    }
    //setResult를 자식 액티비티에서 실행시켰다면 해당 함수가 실행됨
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //만약 ok가 아니면 그냥 통과
        if(resultCode != Activity.RESULT_OK){
            Log.d(TAG,"1.뒤로가기를 해도 정보가 안넘어옴")
            return
        }
        //ok이긴 하지만 다른 setResult를 통해서 넘어왔다면 패스
        if(requestCode== REQUEST_CODE_CHEAT){
            Log.d(TAG,"2.정보는 넘어 왔으나 isCheater값 그대로임")
            if(data?.getBooleanExtra(EXTRA_ANSWER_SHOWN,false)?:false){
                quizViewModel.cheat()
                quizViewModel.cheatCount-=1
                if(quizViewModel.cheatCount==0){
                    cheatButton.isEnabled=false
                }
                Log.d(TAG,"cheatCount ${quizViewModel.cheatCount}")
            }
        }
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

        val messageResId=when{
            quizViewModel.currentQuestionIsCheat->R.string.judgment_toast
            userAnswer == correctAnswer->R.string.correct_toast
            else->R.string.incorrect_toast
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