package com.android.dazncodingchallenge.presentation.event

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.dazncodingchallenge.R
import com.android.dazncodingchallenge.databinding.FragmentListEventsBinding
import com.android.dazncodingchallenge.domain.Events
import com.android.dazncodingchallenge.presentation.OnVideoPlayerCallback
import com.android.dazncodingchallenge.utils.Variables
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventListFragment : Fragment(),OnEventAdapterListener {

    private lateinit var fragmentManufacturersBinding: FragmentListEventsBinding
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var mCallback: OnVideoPlayerCallback? = null
    private var adapter: EventListAdapter? = null
    private var toolbar: Toolbar? = null
    private val viewModel: EventListViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnVideoPlayerCallback) {
            mCallback = context
        } else throw ClassCastException(context.toString() + "must implement OnVideoPlayerCallback!")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = EventListAdapter(this)
        viewModel.loadEventsList()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentManufacturersBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_list_events, container, false)
        fragmentManufacturersBinding.eventListViewModel = viewModel
        fragmentManufacturersBinding.manufacturerRecyclerView.adapter = adapter

        toolbar = fragmentManufacturersBinding.root.findViewById(R.id.toolbar) as Toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar?.title = getString(R.string.str_events)

        //** Set the Layout Manager of the RecyclerView
        setRVLayoutManager()

        setupObservers()

        return fragmentManufacturersBinding.root
    }

    private fun setRVLayoutManager() {
        mLayoutManager = LinearLayoutManager(requireContext())
        fragmentManufacturersBinding.manufacturerRecyclerView.layoutManager = mLayoutManager
        fragmentManufacturersBinding.manufacturerRecyclerView.setHasFixedSize(true)
        fragmentManufacturersBinding.manufacturerRecyclerView.itemAnimator = null
    }


    private fun setupObservers() {

        if (Variables.isNetworkConnected) {
            viewModel.isLoad.observe(viewLifecycleOwner, {
                it?.let { visibility ->
                    fragmentManufacturersBinding.progressBar.visibility =
                        if (visibility) View.GONE else View.VISIBLE
                    fragmentManufacturersBinding.manufacturerRecyclerView.visibility = View.VISIBLE
                    fragmentManufacturersBinding.noNetwork.visibility = View.GONE
                }
            })

            viewModel.eventsReceivedLiveData.observe(viewLifecycleOwner, {
                it?.let {
                    initRecyclerView(it)
                }
            })
        } else {
            viewModel.isLoad.value = true
            viewModel.isLoad.observe(viewLifecycleOwner, {
                it?.let { visibility ->
                    fragmentManufacturersBinding.progressBar.visibility =
                        if (visibility) View.GONE else View.VISIBLE
                    fragmentManufacturersBinding.manufacturerRecyclerView.visibility = View.GONE
                    fragmentManufacturersBinding.noNetwork.visibility = View.VISIBLE
                }
            })
        }
    }

    override fun onDetach() {
        super.onDetach()
        adapter = null
        mCallback = null
    }

    companion object {

        val FRAGMENT_NAME: String = EventListFragment::class.java.name

        @JvmStatic
        fun newInstance() =
            EventListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }


    private fun initRecyclerView(eventList: List<Events>) {
        val sortedList = eventList.sortedWith(compareBy { it.date })
        adapter?.updateData(sortedList)
    }

    override fun showEventList(eventData: Events) {
        mCallback?.navigateToVideoPlayerPage(eventData)
    }
}