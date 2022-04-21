package io.tasks

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var addButton: Button
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: TasksAdapter
    private val tasks = arrayListOf<Task>()

    private var currentId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton = findViewById(R.id.add)
        tasksRecyclerView = findViewById(R.id.tasks)
        tasksAdapter = TasksAdapter(this, tasks)
        tasksRecyclerView.adapter = tasksAdapter

        addButton.setOnClickListener {
            val task = Task(currentId, "Testing $currentId")
            tasks.add(task)
            tasksAdapter.notifyItemInserted(currentId)
            currentId++
        }
    }

}