package com.android.dazncodingchallenge.presentation.event

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
import com.android.dazncodingchallenge.databinding.HolderItemBinding
import com.android.dazncodingchallenge.domain.Events
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

internal class EventListAdapter(val mListener: OnEventAdapterListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var eventList: List<Events> = ArrayList()
    private val picasso = Picasso.get()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderAlbumBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), R.layout.holder_item, parent, false
        )
        return EventViewHolder(holderAlbumBinding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as EventViewHolder).onBind(getItem(position), position)
    }

    private fun getItem(position: Int): Events {
        return eventList[position]
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    fun updateData(events: List<Events>) {
        val diffCallback = EventDiffCallBack(events, eventList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        eventList = events.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    inner class EventViewHolder(private val dataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun onBind(events: Events, position: Int) {
            val holderAlbumBinding = dataBinding as HolderItemBinding
            holderAlbumBinding.events = events
            val date = convertDateString(
                PATTERN_SERVER_DATE_TIME,
                PATTERN_SERVER_DATE,
                events.date
            )
            val formatedDate = convertDateString(
                PATTERN_SERVER_DATE_TIME,
                PATTERN_START_WITH_DAY,
                events.date
            )
            val time = convertDateString(
                PATTERN_SERVER_DATE_TIME,
                PATTERN_TIME_24H,
                events.date
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
            picasso.load(events.imageUrl).into(itemView.profile_pic)
            if (events.imageUrl.isNotEmpty()) {
                itemView.iv_cw_play.visibility = View.VISIBLE
            } else {
                itemView.iv_cw_play.visibility = View.GONE
            }
            itemView.setOnClickListener {
                mListener.showEventList(events)
            }
        }
    }
}
