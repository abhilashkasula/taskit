package io.taskIt.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import io.taskIt.Task

class TasksDatabase(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "taskit"
        private const val TABLE = "tasks"
        private const val ID = "id"
        private const val TASK = "task"
        private const val IS_COMPLETED = "is_completed"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createQuery = "CREATE TABLE $TABLE ($ID INTEGER PRIMARY KEY, $TASK TEXT, $IS_COMPLETED BOOLEAN)"
        db?.execSQL(createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    fun addTask(task: Task) {
        val db = this.writableDatabase
        val values = ContentValues()
        if (task.id != null) {
            values.put(ID, task.id)
        }
        values.put(TASK, task.action)
        values.put(IS_COMPLETED, task.isCompleted)
        db.insert(TABLE, null, values)
    }

    fun getTasks(): ArrayList<Task> {
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE;"
        val cursor = db.rawQuery(query, null)

        val tasks = arrayListOf<Task>()

        Log.d("DB", "getTasks")

        if (cursor.moveToFirst()) {
            do {
                Log.d("DB", "inside")
                val id = cursor.getInt(0)
                val action = cursor.getString(1)
                val isCompleted = cursor.getInt(2) % 2 != 0
                val task = Task(id, action, isCompleted)
                Log.d("DB", task.toString())
                tasks.add(0, task)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return tasks
    }

    fun updateTask(task: Task) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TASK, task.action)
        values.put(IS_COMPLETED, task.isCompleted)
        db.update(TABLE, values, "$ID =?", arrayOf(task.id.toString()))
    }

    fun removeTask(id: Int) {
        this.writableDatabase.delete(TABLE, "$ID =?", arrayOf(id.toString()))
    }
}