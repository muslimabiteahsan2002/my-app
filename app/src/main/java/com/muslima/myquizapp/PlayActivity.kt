package com.muslima.myquizapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.muslima.myquizapp.databinding.ActivityPlayBinding
import java.util.Locale
import java.util.concurrent.TimeUnit

class PlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayBinding

    // timer
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var defaultColor: ColorStateList
    private val countDownInMilliSecond: Long = 30000
    private val countDownInterval: Long = 1000
    private var timeLeftMilliSeconds: Long = 0

    private var score = 0
    private var correct = 0
    private var wrong = 0
    private var skip = 0
    private var qIndex = 0
    private var updateQueNo = 1

    // create string for question, answer and options
    private var questions = arrayOf(
        "Q.1. If a computer has more than one processor then it is known as?",
        "Q.2. Full form of URL is?",
        "Q.3. One kilobyte (KB) is equal to",
        "Q.4. Father of ‘C’ programming language?",
        "Q.5. SMPS stands for",
        "Q.6. What is a floppy disk used for",
        "Q.7. Which operating system is developed and used by Apple Inc?",
        "Q.8. Random Access Memory (RAM) is which storage of device?",
        "Q.9. Who is the founder of the Internet?",
        "Q.10. Which one is the first search engine in internet?"
    )
    private var answer = arrayOf(
        "Multiprocessor",
        "Uniform Resource Locator",
        "1,024 bytes",
        "Dennis Ritchie",
        "Switched mode power supply",
        "To store information",
        "iOS",
        "Primay",
        "Tim Berners-Lee",
        "Archie"
    )
    private var options = arrayOf(
        "Uniprocess",
        "Multiprocessor",
        "Multithreaded",
        "Multiprogramming",
        "Uniform Resource Locator",
        "Uniform Resource Linkwrong",
        "Uniform Registered Link",
        "Unified Resource Link",
        "1,000 bits",
        "1,024 bytes",
        "1,024 megabytes",
        "1,024 gigabytes",
        "Dennis Ritchie",
        "Prof Jhon Kemeny",
        "Thomas Kurtz",
        "Bill Gates",
        "Switched mode power supply",
        "Start mode power supply",
        "Store mode power supply",
        "Single mode power supply",
        "To unlock the computer",
        "To store information",
        "To erase the computer screen",
        "To make the printer work",
        "Windows",
        "Android",
        "iOS",
        "UNIX",
        "Primay",
        "Secondary",
        "Teriary",
        "Off line",
        "Vint Cerf",
        "Charles Babbage",
        "Tim Berners-Lee",
        "None of these",
        "Google",
        "Archie",
        "Altavista",
        "WAIS"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        binding.apply {
            questionTv.text = questions[qIndex]
            option1.text = options[0]
            option2.text = options[1]
            option3.text = options[2]
            option4.text = options[3]

            nextQuizBtn.setOnClickListener {
                if (optionRgp.checkedRadioButtonId == -1) {
                    Toast.makeText(
                        this@PlayActivity,
                        "Please select thee option",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showNextQuestion()
                }
            }

            totalQuizTv.text = "Total :$updateQueNo/${questions.size}"
            questionTv.text = questions[qIndex]
            defaultColor = quizTime.textColors
            timeLeftMilliSeconds = countDownInMilliSecond
            startCountDownTimer()
        }
    }

    private fun startCountDownTimer() {
        countDownTimer = object : CountDownTimer(timeLeftMilliSeconds, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                binding.apply {
                    timeLeftMilliSeconds = millisUntilFinished
                    val second = TimeUnit.MILLISECONDS.toSeconds(timeLeftMilliSeconds).toInt()
                    val timer = String.format(Locale.getDefault(), "Time: %02d", second)
                    quizTime.text = timer
                    if (timeLeftMilliSeconds < 10000) {
                        quizTime.setTextColor(Color.RED)
                    } else {
                        quizTime.setTextColor(defaultColor)
                    }
                }
            }

            override fun onFinish() {
                showNextQuestion()
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun showNextQuestion() {
        checkAnswer()

        binding.apply {
            if (updateQueNo < questions.size) {
                totalQuizTv.text = "${updateQueNo}/${questions.size}"
                updateQueNo++
            }
            if (qIndex <= questions.size - 1) {
                questionTv.text = questions[qIndex]
                option1.text = options[qIndex * 4]
                option2.text = options[qIndex * 4 + 1]
                option3.text = options[qIndex * 4 + 2]
                option4.text = options[qIndex * 4 + 3]
            } else {
                score = correct
                val myIntent = Intent(this@PlayActivity, ResultActivity::class.java)
                myIntent.putExtra(correctKey, correct)
                myIntent.putExtra(wrongKey, wrong)
                myIntent.putExtra(skipKey, skip)
                startActivity(myIntent)
                finish()
            }
            optionRgp.clearCheck()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkAnswer() {
        binding.apply {
            if (optionRgp.checkedRadioButtonId == -1) {
                skip++
                timeOverAlertDialog()
            } else {
                val checkOption = findViewById<RadioButton>(optionRgp.checkedRadioButtonId)
                val checkAnswer = checkOption.text.toString()
                if (checkAnswer == answer[qIndex]) {
                    correct++
                    scoreTv.text = "Score : $correct"
                    correctAlertDialog()
                    countDownTimer.cancel()
                } else {
                    wrong++
                    wrongAlertDialog()
                    countDownTimer.cancel()
                }
            }
            qIndex++
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun wrongAlertDialog() {
        val builder = AlertDialog.Builder(this@PlayActivity)
        val view = LayoutInflater.from(this@PlayActivity).inflate(R.layout.wrong_dialog, null)
        builder.setView(view)
        val wrongTv = view.findViewById<TextView>(R.id.locatorTv)
        val wrongOk = view.findViewById<Button>(R.id.wrongBtn)
        wrongTv.text = "Correct Answer : ${answer[qIndex]}"
        val alertDialog = builder.create()
        wrongOk.setOnClickListener {
            timeLeftMilliSeconds = countDownInMilliSecond
            startCountDownTimer()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun correctAlertDialog() {
        val builder = AlertDialog.Builder(this@PlayActivity)
        val view = LayoutInflater.from(this@PlayActivity).inflate(R.layout.right_dialog, null)
        builder.setView(view)
        val scoreTv = view.findViewById<TextView>(R.id.scoreTv)
        val correctOkBtn = view.findViewById<Button>(R.id.correctBtn)
        scoreTv.text = "Score : $correct"
        val alertDialog = builder.create()
        correctOkBtn.setOnClickListener {
            timeLeftMilliSeconds = countDownInMilliSecond
            startCountDownTimer()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    @SuppressLint("InflateParams")
    private fun timeOverAlertDialog() {
        val builder = AlertDialog.Builder(this@PlayActivity)
        val view = LayoutInflater.from(this@PlayActivity).inflate(R.layout.time_dialog, null)
        builder.setView(view)
        val timeOverOk = view.findViewById<Button>(R.id.timeBtn)
        val alertDialog = builder.create()
        timeOverOk.setOnClickListener {
            timeLeftMilliSeconds = countDownInMilliSecond
            startCountDownTimer()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

}