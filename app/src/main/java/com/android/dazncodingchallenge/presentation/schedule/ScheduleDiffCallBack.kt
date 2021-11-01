package com.android.dazncodingchallenge.presentation.schedule

import androidx.recyclerview.widget.DiffUtil
import com.android.dazncodingchallenge.domain.Schedule

class ScheduleDiffCallBack(
    private val newItems: List<Schedule>,
    private val oldItems: List<Schedule>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return (oldItem.id == newItem.id) && (oldItem.title == newItem.title)
    }

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem == newItem
    }
}