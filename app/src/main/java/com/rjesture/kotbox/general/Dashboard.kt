package com.rjesture.kotbox.general

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rjesture.kotbox.R
import com.rjesture.kotbox.room.ListingPage
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setListeners();

    }

    private fun setListeners() {
        cv_rooms.setOnClickListener {
            val intent = Intent(this, ListingPage::class.java)
            startActivity(intent)

        }
    }
}