package com.example.aircare

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var temperatureChart: LineChart
    private lateinit var humidityChart: LineChart
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        temperatureChart = findViewById(R.id.tempratureLineChart)
        humidityChart = findViewById(R.id.humidityLineChart)

        setUpTemperatureLineChart()
        fetchTemperatureDataAndDrawChart()

        setUpHumidityLineChart()
        fetchHumidityDataAndDrawChart()

    }

    private fun fetchTemperatureDataAndDrawChart() {
        val reference = database.getReference("temperature")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val entries = ArrayList<Entry>()
                for (snapshot in dataSnapshot.children) {
                    val value = snapshot.child("value").getValue(Float::class.java)
                    val time = snapshot.child("time").getValue(String::class.java)
                    value?.let {
                        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
                        val timestamp = format.parse(time).time
                        entries.add(Entry(timestamp.toFloat(), value))
                    }
                }
                updateTemperatureLineChart(entries)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun setUpTemperatureLineChart() {
        temperatureChart.apply {
            animateX(1200, Easing.EaseInSine)
            description.isEnabled = false

            xAxis.apply {
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1F
                valueFormatter = object : ValueFormatter() {
                    private val format = SimpleDateFormat("HH:mm", Locale.getDefault())

                    override fun getFormattedValue(value: Float): String {
                        return format.format(Date(value.toLong()))
                    }
                }
            }

            axisRight.isEnabled = false
            extraRightOffset = 30f

            legend.apply {
                orientation = Legend.LegendOrientation.VERTICAL
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                textSize = 15F
                form = Legend.LegendForm.LINE
            }
        }
    }

    private fun updateTemperatureLineChart(entries: ArrayList<Entry>) {
        val dataSet = LineDataSet(entries, " ")
        dataSet.apply {
            setDrawCircleHole(false)
            circleColors = listOf(Color.BLUE)
            valueTextSize = 12f
            color = Color.BLUE
            valueTextColor = Color.RED
        }

        val lineData = LineData(dataSet)
        temperatureChart.data = lineData
        temperatureChart.invalidate()
    }


    private fun fetchHumidityDataAndDrawChart() {
        val reference = database.getReference("humidity")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val entries = ArrayList<Entry>()
                for (snapshot in dataSnapshot.children) {
                    val value = snapshot.child("value").getValue(Float::class.java)
                    val time = snapshot.child("time").getValue(String::class.java)
                    value?.let {
                        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
                        val timestamp = format.parse(time).time
                        entries.add(Entry(timestamp.toFloat(), value))
                    }
                }
                updateLineChart(entries)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun setUpHumidityLineChart() {
        humidityChart.apply {
            animateX(1200, Easing.EaseInSine)
            description.isEnabled = false

            xAxis.apply {
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1F
                valueFormatter = object : ValueFormatter() {
                    private val format = SimpleDateFormat("HH:mm", Locale.getDefault())

                    override fun getFormattedValue(value: Float): String {
                        return format.format(Date(value.toLong()))
                    }
                }
            }

            axisRight.isEnabled = false
            extraRightOffset = 30f

            legend.apply {
                orientation = Legend.LegendOrientation.VERTICAL
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                textSize = 15F
                form = Legend.LegendForm.LINE
            }
        }
    }

    private fun updateLineChart(entries: ArrayList<Entry>) {
        val dataSet = LineDataSet(entries, " ")
        dataSet.apply {
            setDrawCircleHole(true)
            circleColors = listOf(Color.CYAN)
            valueTextSize = 12f
            color = Color.CYAN
            valueTextColor = Color.RED
        }

        val lineData = LineData(dataSet)
        humidityChart.data = lineData
        humidityChart.invalidate()
    }

}
