package io.tasks

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var addButton: FloatingActionButton
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: TasksAdapter
    private val tasks = arrayListOf<Task>()

    private var currentId: Int = 0

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
        val task = Task(currentId, action)
        tasks.add(0, task)
        tasksAdapter.notifyItemInserted(0)
        currentId++
    }

    private fun updateTask(position: Int, isCompleted: Boolean) {
        tasks[position].isCompleted = isCompleted
        if (!tasksRecyclerView.isComputingLayout) {
            tasksAdapter.notifyItemChanged(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton = findViewById(R.id.add)
        tasksRecyclerView = findViewById(R.id.tasks)
        tasksAdapter = TasksAdapter(this, tasks, ::updateTask)
        tasksRecyclerView.adapter = tasksAdapter

        addButton.setOnClickListener {
            getContent.launch(Intent(this, AddTask::class.java))
        }
    }

}