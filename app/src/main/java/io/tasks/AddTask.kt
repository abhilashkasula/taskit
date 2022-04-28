package io.tasks

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddTask : AppCompatActivity() {
    private lateinit var taskText: EditText
    private lateinit var completeButton: ExtendedFloatingActionButton
    private lateinit var cancelButton: ExtendedFloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        completeButton = findViewById(R.id.complete)
        cancelButton = findViewById(R.id.cancel)
        taskText = findViewById(R.id.taskEditor)
        completeButton.setOnClickListener(::add)
        cancelButton.setOnClickListener(::cancel)
    }

    private fun cancel(view: View?) {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun add(view: View?) {
        val task = taskText.text.toString().trim()
        if (task.isEmpty()) {
            taskText.setHintTextColor(resources.getColor(R.color.red, null))
            return
        }
        val intent = Intent()
        intent.putExtra("action", task)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}