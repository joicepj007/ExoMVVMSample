package com.android.dazncodingchallenge.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.dazncodingchallenge.R
import com.android.dazncodingchallenge.domain.Events
import com.android.dazncodingchallenge.presentation.event.EventListFragment
import com.android.dazncodingchallenge.presentation.player.PlayerActivity
import com.android.dazncodingchallenge.presentation.schedule.ScheduleListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnVideoPlayerCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val eventListFragment=
            EventListFragment()
        val scheduleListFragment=
            ScheduleListFragment()

        setCurrentFragment(eventListFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.event ->setCurrentFragment(eventListFragment)
                R.id.schedule ->setCurrentFragment(scheduleListFragment)

            }
            true
        }

    }

    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    override fun navigateToVideoPlayerPage(manufactureData: Events) {

        val intent = Intent(this, PlayerActivity::class.java)
        val bundle = Bundle().apply {
            putString(KEY_VIDEO_URL, manufactureData.videoUrl)
        }
        intent.putExtras(bundle)
        startActivity(intent)
    }

    companion object {
        private const val KEY_VIDEO_URL = "KEY_VIDEO_URL"
    }

}