package io.tasks

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TasksAdapter(private val context: Context, private val tasks: ArrayList<Task>): RecyclerView.Adapter<TasksAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.action)
        var checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("Test", position.toString() + " ${tasks[position]}")
        holder.textView.text = tasks[position].action
        holder.checkBox.buttonTintList = getColorList(position)
    }

    private fun getColorList(position: Int): ColorStateList? {
        if (position % 2 == 0) {
            return context.getColorStateList(R.color.checkbox_pink)
        }
        return context.getColorStateList(R.color.checkbox_blue)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}