package com.poc.expandablelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.poc.expandablelist.CityListScreen
import com.poc.expandablelist.viewmodel.CityViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: CityViewModel = viewModel { CityViewModel(applicationContext) }
            CityListScreen(viewModel)
        }
    }
}
