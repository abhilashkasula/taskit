package io.tasks

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TasksAdapter(
    private val context: Context,
    private val tasks: ArrayList<Task>,
    private val updateTasks: (Int, Boolean) -> Unit
) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.action)
        var checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        Log.d("Test", "$position $task")
        holder.textView.text = task.action
        applyStrikeThrough(holder.textView, task.isCompleted)
        holder.checkBox.buttonTintList = getColorList(position, task.isCompleted)
        holder.checkBox.isChecked = task.isCompleted
        holder.checkBox.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            updateTasks(holder.adapterPosition, isChecked)
        }
    }

    private fun applyStrikeThrough(textView: TextView, isStrikeNeeded: Boolean) {
        if (isStrikeNeeded) {
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            return
        }
        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }

    private fun getColorList(position: Int, completed: Boolean): ColorStateList {
        if (completed) {
            return context.getColorStateList(R.color.checkbox_grey)
        }
        if (position % 2 == 0) {
            return context.getColorStateList(R.color.checkbox_pink)
        }
        return context.getColorStateList(R.color.checkbox_blue)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}