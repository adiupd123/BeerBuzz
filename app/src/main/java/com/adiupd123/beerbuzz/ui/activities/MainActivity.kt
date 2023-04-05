package com.adiupd123.beerbuzz.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adiupd123.beerbuzz.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}