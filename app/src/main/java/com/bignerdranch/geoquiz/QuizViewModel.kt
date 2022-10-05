package com.bignerdranch.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG="QuizViewModel"
class QuizViewModel : ViewModel(){



    private val questionBank = listOf(
        Question(R.string.question_australia,true,false),
        Question(R.string.question_oceans,true,false),
        Question(R.string.question_mideast,false,false),
        Question(R.string.question_africa,false,false),
        Question(R.string.question_americas,true,false),
        Question(R.string.question_asia,true,false))

    var currentIndex=0
    var wrongAnswer=0
    var correctANswer=0

    val currentQuestionAnswer:Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText:Int
        get()=questionBank[currentIndex].textResId

    val currentQuestionCorrect:Boolean
        get()=questionBank[currentIndex].correct

    fun moveToNext(){
        currentIndex=(currentIndex+1)%questionBank.size
    }
    fun checkNext():Boolean{
        if(currentIndex==questionBank.size-1){
            return true
        }
        else{
            return false
        }
    }

    fun answered(){
        questionBank[currentIndex].correct=true
    }

    fun upCorrect(){
        correctANswer+=1
    }

    fun upWrong(){
        wrongAnswer+=1
    }

    fun getScore():Int{
        if (correctANswer+wrongAnswer==0){
            return 0}
        return (100*correctANswer/(correctANswer+wrongAnswer))
    }
}