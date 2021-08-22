package com.poema.runnerapp2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.poema.runnerapp2.R
import com.poema.runnerapp2.db.Run
import com.poema.runnerapp2.other.TrackingUtility
import com.poema.runnerapp2.repositories.MainRepository
import kotlinx.android.synthetic.main.item_run.view.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter (
    val repo: MainRepository,
    val context: Context): RecyclerView.Adapter<RunAdapter.RunViewHolder>() {


    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val delete : ImageView = itemView.findViewById(R.id.deleteImageView)

        init{
            delete.setOnClickListener{ view ->

                    val thisRun = differ.currentList[adapterPosition]
                    val dialogBuilder = AlertDialog.Builder(context)

                    dialogBuilder.setTitle("Delete run")
                        .setMessage("Are you sure you want to delete this run?")
                        .setIcon(0)
                        .setPositiveButton("Delete run") { _, _ ->
                            deleteRun(thisRun)
                            Snackbar.make(view, "Run deleted", Snackbar.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.cancel()
                        }

                    val alert = dialogBuilder.create()
                    alert.show()
                }
            }

    }

    val diffCallback = object : DiffUtil.ItemCallback<Run>(){
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
           return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Run>) = differ.submitList(list)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_run,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RunAdapter.RunViewHolder, position: Int) {

       val run = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(run.img).into(ivRunImage)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timestamp
            }
            val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            tvDate.text = dateFormat.format(calendar.time)

            val avgSpeed = "${"%.1f".format(run.avgSpeedInKMH)}km/h"

            tvAvgSpeed.text = avgSpeed

            val distanceInKM = "${"%.2f".format(run.distanceInMeters/1000f)}km"
            tvDistance.text = distanceInKM

            tvTime.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)

            val caloriesBurned = "${run.caloriesBurned}kcal"
            tvCalories.text = caloriesBurned

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun deleteRun(run:Run){
        CoroutineScope(Dispatchers.IO ).launch {
            repo.deleteRun(run)
        }
    }

}