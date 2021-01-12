package com.example.myapplication.thirdTab

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import kotlinx.android.synthetic.main.edit_appbar.*
import kotlinx.android.synthetic.main.time_box.*
import java.util.*

class ScheduleEditActivity : AppCompatActivity() {

    val REQUEST_ADD = 1
    val REQUEST_EDIT = 2
    val RESULT_OK_ADD = 1
    val RESULT_OK_EDIT = 2
    val RESULT_OK_DELETE = 3

    private var mode = 0
    private var schedule: Schedule? = null
    private var editIdx = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_edit)
        init()
    }

    private fun init(){
        //set the default time
        schedule = Schedule()
        schedule!!.startTime = Time(10, 0)
        schedule!!.endTime = Time(13, 30)
        checkMode()
        initView()
    }

    private fun checkMode() {
        val i = intent
        mode = i.getIntExtra("mode", REQUEST_ADD)
        if (mode == REQUEST_EDIT) {
            loadScheduleData()
            delete_btn.visibility = View.VISIBLE
        }
    }

    private fun initView() {
        submit_btn.setOnClickListener{
            if (mode == REQUEST_ADD) {
                inputDataProcessing()
                val i = Intent()
                val schedules = ArrayList<Schedule>()
                //you can add more schedules to ArrayList
                schedules.add(schedule!!)
                i.putExtra("schedules", schedules)
                setResult(RESULT_OK_ADD, i)
                finish()
            } else if (mode == REQUEST_EDIT) {
                inputDataProcessing()
                val i = Intent()
                val schedules = ArrayList<Schedule>()
                schedules.add(schedule!!)
                i.putExtra("idx", editIdx)
                i.putExtra("schedules", schedules)
                setResult(RESULT_OK_EDIT, i)
                finish()
            }
        }
        delete_btn.setOnClickListener{
            val i = Intent()
            i.putExtra("idx", editIdx)
            setResult(RESULT_OK_DELETE, i)
            finish()
        }
        day_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                schedule!!.day = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        start_time.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val dialog = TimePickerDialog(
                    this@ScheduleEditActivity,
                    listener,
                    schedule!!.startTime.hour,
                    schedule!!.startTime.minute,
                    false
                )
                dialog.show()
            }

            private val listener =
                OnTimeSetListener { view, hourOfDay, minute ->
                    start_time.text = "$hourOfDay:$minute"
                    schedule!!.startTime.hour = hourOfDay
                    schedule!!.startTime.minute = minute
                }
        })
        end_time.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val dialog = TimePickerDialog(
                    this@ScheduleEditActivity,
                    listener,
                    schedule!!.endTime.hour,
                    schedule!!.endTime.minute,
                    false
                )
                dialog.show()
            }

            private val listener =
                OnTimeSetListener { view, hourOfDay, minute ->
                    end_time.text = "$hourOfDay:$minute"
                    schedule!!.endTime.hour = hourOfDay
                    schedule!!.endTime.minute = minute
                }
        })
    }

    private fun loadScheduleData() {
        val i = intent
        editIdx = i.getIntExtra("idx", -1)
        val schedules =
            i.getSerializableExtra("schedules") as ArrayList<Schedule>?
        schedule = schedules!![0]
        subject_edit.setText(schedule!!.classTitle)
        classroom_edit.setText(schedule!!.classPlace)
        professor_edit.setText(schedule!!.professorName)
        day_spinner.setSelection(schedule!!.day)
    }

    private fun inputDataProcessing() {
        schedule!!.classTitle = subject_edit.getText().toString()
        schedule!!.classPlace = classroom_edit.getText().toString()
        schedule!!.professorName = professor_edit.getText().toString()
    }

}


