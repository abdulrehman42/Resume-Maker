package com.example.resumemaker.views.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resumemaker.databinding.ActivityBoardingScreenBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.BoardingAdapter

class BoardingScreen : AppCompatActivity() {
    lateinit var binding: ActivityBoardingScreenBinding
    lateinit var boardingAdapter: BoardingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBoardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAdapter()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() {
        boardingAdapter = BoardingAdapter(this, Helper.getBoardingItem()) { i ->
            Helper.checkDots(i,binding)
        }


        binding.recyclerviewBoarding.apply {
            setLayoutManager(
                LinearLayoutManager(
                    this@BoardingScreen,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            adapter = boardingAdapter
            boardingAdapter.notifyDataSetChanged()
        }
       // Helper.checkDots(binding.recyclerviewBoarding.getCurrentPosition(),binding)

    }
    fun RecyclerView?.getCurrentPosition() : Int {
        return (this?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    }
}