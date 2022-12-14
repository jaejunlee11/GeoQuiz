package com.bignerdranch.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

const val EXTRA_ANSWER_SHOWN="com.bignerdranch.android.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE="com.bignerdranch.android.geoquiz.answer_is_true"
private const val EXTRA_CHEAT_COUNT="com.bignerdranch.android.geoquiz.cheat_count"
class CheatActivity : AppCompatActivity() {

//1.quizVeiwModel은 데이터 베이스처럼 공유하는 것은 아니다.
//2.각각의 인스턴스를 만들었을 뿐 공유해서 데이터 꺼내기는 불가능
    private val quizViewModel:QuizViewModel by lazy{
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }
    private lateinit var answerTextView:TextView
    private lateinit var apiTextView:TextView
    private lateinit var cheatTextView:TextView
    private lateinit var showAnswerButton:Button

    private var answerIsTrue=false
    private var cheatCount=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        //intent로 받았던 값을 꺼내서 담음
        answerIsTrue=intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false)
        cheatCount=intent.getIntExtra(EXTRA_CHEAT_COUNT,3)
        answerTextView=findViewById(R.id.answer_text_view)
        showAnswerButton=findViewById(R.id.show_answer_button)
        apiTextView=findViewById(R.id.api_version)
        cheatTextView=findViewById(R.id.cheat_count)
        //api레벨 보여주기
        apiTextView.setText("API 레벨 ${Build.VERSION.SDK_INT}")

        //답 보여주기 버튼
        showAnswerButton.setOnClickListener {
            quizViewModel.isCheater=true
            update()
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
        fun newIntent(packageContext: Context,answerIsTrue:Boolean,cheatCount:Int): Intent {
            return Intent(packageContext,CheatActivity::class.java).apply{
                putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue)
                putExtra(EXTRA_CHEAT_COUNT, cheatCount)
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
            cheatTextView.setText("남은 커닝 수:${cheatCount-1}")
        }else{
            cheatTextView.setText("남은 커닝 수:${cheatCount}")
        }
    }
}
