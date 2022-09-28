package com.amaromerovic.ageinminutes

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amaromerovic.ageinminutes.databinding.ActivityMainBinding
import com.google.android.material.R
import com.google.android.material.snackbar.Snackbar
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dateButton.setOnClickListener {

            val cal = Calendar.getInstance(TimeZone.getDefault())
            val y = cal.get(Calendar.YEAR)
            val m = cal.get(Calendar.MONTH)
            val d = cal.get(Calendar.DAY_OF_MONTH)



            DatePickerDialog(
                this,
                { _, year, month, day ->

                    val calTwo = Calendar.getInstance(TimeZone.getDefault())
                    calTwo.set(year, month, day)
                    setTextToViews(day, month, year, cal.timeInMillis, calTwo.timeInMillis)

                }, y, m, d
            ).show()

        }

    }

    private fun setTextToViews(day: Int, month: Int, year: Int, currentDate: Long, birthDate: Long) {

        if (binding.dateTextView.visibility == View.GONE && binding.selectedDateHint.visibility == View.GONE) {
            binding.dateTextView.visibility = View.VISIBLE
            binding.dateTextView.text = Editable.Factory.getInstance()
                .newEditable("$day/${month + 1}/$year")
            binding.selectedDateHint.visibility = View.VISIBLE
        }

        if (currentDate <= birthDate) {
            if (binding.minutesTextView.visibility != View.GONE && binding.minutesHint.visibility != View.GONE) {
                binding.minutesTextView.visibility = View.GONE
                binding.minutesHint.visibility = View.GONE
            }
            showSnackbar()
        } else {
            if (binding.minutesTextView.visibility == View.GONE && binding.minutesHint.visibility == View.GONE) {
                binding.minutesTextView.visibility = View.VISIBLE
                binding.minutesHint.visibility = View.VISIBLE
            }
            binding.minutesTextView.text = Editable.Factory.getInstance().newEditable(
                "${
                    TimeUnit.MILLISECONDS.toMinutes(currentDate - birthDate) + 1
                }"
            )
        }


    }


    private fun showSnackbar() {
        snackbar = Snackbar.make(
            this.binding.root,
            "Woo, are you from the future?!",
            Snackbar.LENGTH_SHORT
        )
            .setBackgroundTint(
                ContextCompat.getColor(
                    this,
                    com.amaromerovic.ageinminutes.R.color.lightOrange
                )
            )
        val view = snackbar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM or Gravity.CENTER
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT
        params.bottomMargin = 60
        val tv = view.findViewById<TextView>(R.id.snackbar_text)
        if (tv != null) {
            tv.gravity = Gravity.CENTER
            tv.textAlignment = View.TEXT_ALIGNMENT_GRAVITY
        }
        view.layoutParams = params
        snackbar.show()
    }

}



