package com.android.dazncodingchallenge.presentation.schedule


import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.dazncodingchallenge.R
import com.android.dazncodingchallenge.databinding.FragmentListSchedulesBinding
import com.android.dazncodingchallenge.domain.Schedule
import com.android.dazncodingchallenge.utils.PATTERN_SERVER_DATE_TIME
import com.android.dazncodingchallenge.utils.Variables
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ScheduleListFragment : Fragment() {

    private lateinit var fragmentListSchedulesBinding: FragmentListSchedulesBinding
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var adapter: ScheduleListAdapter? = null
    private var toolbar: Toolbar? = null
    private val viewModel: ScheduleListViewModel by viewModels()
    private var handler: Handler = Handler()
    private var runnable: Runnable? = null
    private var delay = 30000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ScheduleListAdapter()
        viewModel.loadScheduleList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentListSchedulesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_list_schedules, container, false)
        fragmentListSchedulesBinding.scheduleListViewModel = viewModel
        fragmentListSchedulesBinding.manufacturerRecyclerView.adapter = adapter

        toolbar = fragmentListSchedulesBinding.root.findViewById(R.id.toolbar) as Toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar?.title = getString(R.string.str_schedules)

        //** Set the Layout Manager of the RecyclerView
        setRVLayoutManager()

        setupObservers()

        return fragmentListSchedulesBinding.root
    }

    private fun setRVLayoutManager() {
        mLayoutManager = LinearLayoutManager(requireContext())
        fragmentListSchedulesBinding.manufacturerRecyclerView.layoutManager = mLayoutManager
        fragmentListSchedulesBinding.manufacturerRecyclerView.setHasFixedSize(true)
        fragmentListSchedulesBinding.manufacturerRecyclerView.itemAnimator = null
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupObservers() {

        if (Variables.isNetworkConnected) {
            viewModel.isLoad.observe(viewLifecycleOwner, {
                it?.let { visibility ->
                    fragmentListSchedulesBinding.progressBar.visibility =
                        if (visibility) View.GONE else View.VISIBLE
                    fragmentListSchedulesBinding.manufacturerRecyclerView.visibility = View.VISIBLE
                    fragmentListSchedulesBinding.noNetwork.visibility = View.GONE
                }
            })

            viewModel.scheduleReceivedLiveData.observe(viewLifecycleOwner, {
                it?.let {
                    initRecyclerView(it)
                }
            })
        } else {
            viewModel.isLoad.value = true
            viewModel.isLoad.observe(viewLifecycleOwner, {
                it?.let { visibility ->
                    fragmentListSchedulesBinding.progressBar.visibility =
                        if (visibility) View.GONE else View.VISIBLE
                    fragmentListSchedulesBinding.manufacturerRecyclerView.visibility = View.GONE
                    fragmentListSchedulesBinding.noNetwork.visibility = View.VISIBLE
                }
            })
        }
    }

    companion object {

        val FRAGMENT_NAME: String = ScheduleListFragment::class.java.name

        @JvmStatic
        fun newInstance() =
            ScheduleListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRecyclerView(eventList: List<Schedule>) {
        val sortedList = eventList.sortedWith(compareBy { it.date })
        adapter?.updateData(sortedList)
    }

    override fun onResume() {
        handler.postDelayed(Runnable {
            runnable?.let { handler.postDelayed(it, delay.toLong()) }
            Toast.makeText(requireContext(), "Auto Refresh in Every 30 seconds", Toast.LENGTH_SHORT).show()
            viewModel.loadScheduleList()
        }.also { runnable = it }, delay.toLong())
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable!!)
    }
}