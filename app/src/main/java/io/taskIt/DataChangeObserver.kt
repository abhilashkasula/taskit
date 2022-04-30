package io.taskIt

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DataChangeObserver(private val recyclerView: RecyclerView, private val view: View, private val adapter: TasksAdapter): RecyclerView.AdapterDataObserver() {

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        updateUI()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        updateUI()
    }

    private fun updateUI() {
        if (adapter.itemCount == 0) {
            recyclerView.visibility = View.GONE
            view.visibility = View.VISIBLE
            return
        }

        recyclerView.visibility = View.VISIBLE
        view.visibility = View.GONE
    }
}