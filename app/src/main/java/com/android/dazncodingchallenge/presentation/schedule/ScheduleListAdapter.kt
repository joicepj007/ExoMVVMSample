package com.android.dazncodingchallenge.presentation.schedule

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.dazncodingchallenge.R
import com.android.dazncodingchallenge.databinding.HolderScheduleItemBinding
import com.android.dazncodingchallenge.domain.Schedule
import com.android.dazncodingchallenge.utils.PATTERN_SERVER_DATE
import com.android.dazncodingchallenge.utils.PATTERN_SERVER_DATE_TIME
import com.android.dazncodingchallenge.utils.PATTERN_START_WITH_DAY
import com.android.dazncodingchallenge.utils.PATTERN_TIME_24H
import com.android.dazncodingchallenge.utils.convertDateString
import com.android.dazncodingchallenge.utils.dateTimeConversionCheck
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.holder_item.view.*
import java.time.LocalDate
import java.util.ArrayList

internal class ScheduleListAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var scheduleList: List<Schedule> = ArrayList()
    private val picasso = Picasso.get()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderScheduleBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), R.layout.holder_schedule_item, parent, false
        )
        return ScheduleViewHolder(holderScheduleBinding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ScheduleViewHolder).onBind(getItem(position), position)
    }

    private fun getItem(position: Int): Schedule {
        return scheduleList[position]
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    fun updateData(schedule: List<Schedule>) {
        val diffCallback = ScheduleDiffCallBack(schedule, scheduleList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        scheduleList = schedule.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }


    inner class ScheduleViewHolder(private val dataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun onBind(schedule: Schedule, position: Int) {
            val holderAlbumBinding = dataBinding as HolderScheduleItemBinding
            holderAlbumBinding.schedules = schedule
            val date = convertDateString(
                PATTERN_SERVER_DATE_TIME,
                PATTERN_SERVER_DATE,
                schedule.date
            )
            val formatedDate = convertDateString(
                PATTERN_SERVER_DATE_TIME,
                PATTERN_START_WITH_DAY,
                schedule.date
            )
            val time = convertDateString(
                PATTERN_SERVER_DATE_TIME,
                PATTERN_TIME_24H,
                schedule.date
            )
            val dateTime = dateTimeConversionCheck(date)
            when {
                dateTime.toLocalDate().equals(LocalDate.now()) -> {
                    itemView.date.text = "Today, $time"
                }
                dateTime.toLocalDate().equals(LocalDate.now().minusDays(1)) -> {
                    itemView.date.text = "Yesterday, $time"
                }
                dateTime.toLocalDate().equals(LocalDate.now().plusDays(1)) -> {
                    itemView.date.text = "Tomorrow, $time"
                }
                else -> {
                    itemView.date.text = formatedDate
                }
            }
            picasso.load(schedule.imageUrl).into(itemView.profile_pic)
            if (schedule.imageUrl.isNotEmpty()) {
                itemView.iv_cw_play.visibility = View.VISIBLE
            } else {
                itemView.iv_cw_play.visibility = View.GONE
            }
        }
    }
}