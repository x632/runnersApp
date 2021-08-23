package com.poema.runnerapp2.ui.fragments


import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.poema.runnerapp2.R
import com.poema.runnerapp2.other.CustomMarkerView
import com.poema.runnerapp2.other.TrackingUtility
import com.poema.runnerapp2.ui.viewmodels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_statistics.*
import java.lang.Math.round

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val viewModel: StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupBarChart()
    }

    private fun setupBarChart(){
        barChart.xAxis.apply{
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.axisLeft.apply{
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.axisRight.apply{
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.apply{
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }

    }

    private fun subscribeToObservers() {
        viewModel.totalTimeRun.observe(viewLifecycleOwner, {
            it?.let {
                val totalTimeRun = TrackingUtility.getFormattedStopWatchTime(it)
                tvTotalTime.text = totalTimeRun
            }
        })
        viewModel.totalDistance.observe(viewLifecycleOwner, {
            it?.let {
                val km = it / 1000f
                //val totalDistance = round(km * 10f) / 10f
                val totalDistanceString = "${"%.1f".format(km)}km"
                tvTotalDistance.text = totalDistanceString
            }
        })
        viewModel.totalAvgSpeed.observe(viewLifecycleOwner, {
            it?.let {
                val avgSpeed = "${"%.2f".format(it)}km/h"
                //val avgSpeed = round(it * 10f) / 10f
                //val avgSpeedString = "${avgSpeed}km/h"
                //val avgSpeedString = avgSpeed
                tvAverageSpeed.text = avgSpeed
            }
        })
        viewModel.totalCaloriesBurned.observe(viewLifecycleOwner, {
            it?.let {
                val totalCalories = "${it}kcal"
                tvTotalCalories.text = totalCalories
            }
        })
        viewModel.runsSortedByDate.observe(viewLifecycleOwner, {
            it?.let{

                val allAvgSpeeds = it.indices.map {i -> BarEntry(i.toFloat(),it[it.lastIndex-i].avgSpeedInKMH*10) }




                val bardataset = BarDataSet(allAvgSpeeds, "Avg Speed Over Time").apply{
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                }
                barChart.data = BarData(bardataset)
                barChart.marker = CustomMarkerView(it.reversed(), requireContext(),R.layout.marker_view)

                barChart.invalidate()
            }
        })
    }
}