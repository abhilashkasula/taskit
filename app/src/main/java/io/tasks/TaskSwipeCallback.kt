package io.tasks

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class TaskSwipeCallback(
    private val tasks: ArrayList<Task>,
    private val tasksAdapter: TasksAdapter,
    private val tasksRecyclerView: RecyclerView
) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
        val position = holder.adapterPosition
        val task = tasks[position]
        tasks.remove(task)
        tasksAdapter.notifyItemRemoved(position)

        Snackbar.make(tasksRecyclerView, "The task was deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                tasks.add(position, task)
                tasksAdapter.notifyItemInserted(position)
            }.show()
    }
}