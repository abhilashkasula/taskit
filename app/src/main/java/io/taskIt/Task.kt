package io.taskIt

data class Task (
    var id: Int? = null,
    var action: String,
    var isCompleted: Boolean = false
)
