package io.tasks

data class Task (
    var id: Int,
    var action: String,
    var isCompleted: Boolean = false
)
