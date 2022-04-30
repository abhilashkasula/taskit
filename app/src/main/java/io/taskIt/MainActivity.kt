package io.taskIt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.snackbar.Snackbar
import io.taskIt.db.TasksDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var addButton: FloatingActionButton
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var emptyTasks: LinearLayout
    private lateinit var tasksProgress: LinearProgressIndicator
    private val tasks = arrayListOf<Task>()
    private val db = TasksDatabase(this)

    private val getContent: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let {intent ->
                addTask(intent.getStringExtra("action"))
            }
        }
    }

    private fun addTask(action: String?) {
        if (action.isNullOrEmpty()) {
            return Toast.makeText(this, "Please enter a valid task", Toast.LENGTH_SHORT).show()
        }
        val task = Task(action = action)
        tasks.add(0, task)
        tasksAdapter.notifyItemInserted(0)
        db.addTask(task)

        updateProgress()
    }

    private fun updateProgress() {
        val tasksCompleted = tasks.filter { it.isCompleted }.size
        val total = tasks.size
        val progress = (tasksCompleted / total.toFloat()) * 100
        tasksProgress.progress = progress.toInt()
    }

    private fun onTaskCompletionStatusChanged(position: Int, isCompleted: Boolean) {
        val task = tasks[position]
        task.isCompleted = isCompleted
        db.updateTask(task)
        updateProgress()
        if (!tasksRecyclerView.isComputingLayout) {
            tasksAdapter.notifyItemChanged(position)
        }
    }

    private fun onTaskRemoved(position: Int) {
        val task = tasks[position]
        tasks.remove(task)
        task.id?.let { db.removeTask(it) }
        tasksAdapter.notifyItemRemoved(position)
        updateProgress()

        Snackbar.make(tasksRecyclerView, "The task was deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                tasks.add(position, task)
                db.addTask(task)
                tasksAdapter.notifyItemInserted(position)
                updateProgress()
            }.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton = findViewById(R.id.add)
        tasksRecyclerView = findViewById(R.id.tasks)
        emptyTasks = findViewById(R.id.emptyTasks)
        tasksProgress = findViewById(R.id.tasksProgress)
        tasksAdapter = TasksAdapter(this, tasks, ::onTaskCompletionStatusChanged)
        tasksAdapter.registerAdapterDataObserver(DataChangeObserver(tasksRecyclerView, emptyTasks, tasksAdapter))
        tasksRecyclerView.adapter = tasksAdapter

        addButton.setOnClickListener {
            getContent.launch(Intent(this, AddTaskActivity::class.java))
        }

        ItemTouchHelper(TaskSwipeCallback(::onTaskRemoved)).attachToRecyclerView(tasksRecyclerView)
        loadTasks()
    }

    private fun loadTasks() {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            val tasksLoadedFromDB = db.getTasks()
            tasksLoadedFromDB.forEach { tasks.add(it) }
            tasksAdapter.notifyDataSetChanged()
            withContext(Dispatchers.Main.immediate) {
                updateProgress()
            }
        }
    }

}