package io.tasks

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddTask : AppCompatActivity() {
    private lateinit var completeButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        completeButton = findViewById(R.id.complete)
        completeButton.setOnClickListener(::add)
    }

    private fun add(view: View) {
        val intent = Intent()
        intent.putExtra("action", "Testing")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}