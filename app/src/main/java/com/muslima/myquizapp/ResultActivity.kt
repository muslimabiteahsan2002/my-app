package com.muslima.myquizapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.muslima.myquizapp.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var totalScore = 0
    private var correct = 0
    private var wrong = 0
    private var skip = 0
    private var isKey = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        totalScore = intent.extras!!.getInt(correctKey)
        wrong = intent.extras!!.getInt(wrongKey)
        skip = intent.extras!!.getInt(skipKey)

        initializeViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initializeViews() {
        binding.apply {
            correctTv.text = "Correct : $totalScore"
            scoreTv.text = "Score : $totalScore"
            wrongTv.text = "Wrong : $wrong"
            skipTv.text = "Skip : $skip"
            if (totalScore >= 6) {
                iconImg.setImageResource(R.drawable.emo_correct)
                Toast.makeText(applicationContext, "New Great", Toast.LENGTH_SHORT).show()
            }  else {
                iconImg.setImageResource(R.drawable.emg_wrong)
                Toast.makeText(applicationContext, "New Great", Toast.LENGTH_SHORT).show()
            }
            playAgainTv.setOnClickListener {
                finish()
            }
        }
    }
}