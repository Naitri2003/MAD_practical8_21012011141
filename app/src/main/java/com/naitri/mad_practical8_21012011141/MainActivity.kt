package com.naitri.mad_practical8_21012011141

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val cardView = findViewById<MaterialCardView>(R.id.card2)
        cardView.visibility=View.GONE
        val createAlarm = findViewById<MaterialButton>(R.id.CreateBtn)
        createAlarm.setOnClickListener{
            //cardView.visibility=View.VISIBLE
            TimePickerDialog(this, { tp, hour, minute -> setAlarmTime(hour, minute) },Calendar.HOUR, Calendar.MINUTE,false).show()
        }
        val cancelAlarm : MaterialButton = findViewById(R.id.CancelBtn)
        cancelAlarm.setOnClickListener {
            stop()
            cardView.visibility = View.GONE
        }
    }
    fun setAlarmTime(hour:Int,minute:Int)
    {
        val cardView=findViewById<MaterialCardView>(R.id.card2)
        cardView.visibility=View.GONE
        val alarmtime=Calendar.getInstance()
        val year=alarmtime.get(Calendar.YEAR)
        val month=alarmtime.get(Calendar.MONTH)
        val date=alarmtime.get(Calendar.DATE)
        alarmtime.set(year, month, date, hour, minute)
        setAlarm(alarmtime.timeInMillis,AlarmBoradcastReceiver.ALARMSTART)
        //cardView.visibility=View.GONE
    }


    fun stop()
    {
        setAlarm(-1,AlarmBoradcastReceiver.ALARMSTOP)
    }

    fun setAlarm(millitime:Long,action:String) {
        val intentalarm = Intent(applicationContext, AlarmBoradcastReceiver::class.java)
        intentalarm.putExtra(AlarmBoradcastReceiver.ALARMKEY, action)
        val pendingintent = PendingIntent.getBroadcast(
            applicationContext,
            4356,
            intentalarm,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmmanager = getSystemService(ALARM_SERVICE) as AlarmManager
        val card : MaterialCardView = findViewById(R.id.card2)
        card.visibility = View.VISIBLE
        if (action ==AlarmBoradcastReceiver.ALARMSTART) {
            alarmmanager.setExact(AlarmManager.RTC_WAKEUP, millitime, pendingintent)
        } else if (action == AlarmBoradcastReceiver.ALARMSTOP){
            alarmmanager.cancel(pendingintent)
            sendBroadcast(intentalarm)
        }
    }
    private fun sendDialogDataToActivity(hour: Int, minute: Int){
        val textAlarmTime : TextView = findViewById(R.id.text)
        val alarmCalendar = Calendar.getInstance()
        val year: Int = alarmCalendar.get(Calendar.YEAR)
        val month: Int = alarmCalendar.get(Calendar.MONTH)
        val day: Int = alarmCalendar.get(Calendar.DATE)
        alarmCalendar.set(year, month, day, hour, minute, 0)
        textAlarmTime.text = SimpleDateFormat("hh:mm:ss a").format(alarmCalendar.time)
        setAlarm(alarmCalendar.timeInMillis,"Start")
        Toast.makeText(
            this,
            "Time: hours:${hour}, minutes:${minute}," +
                    "millis:${alarmCalendar.timeInMillis}",
            Toast.LENGTH_LONG
        ).show()


    }

}
