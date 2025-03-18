package com.poc.expandablelist.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poc.expandablelist.data.City
import com.poc.expandablelist.data.StateGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class CityViewModel(private val context: Context) : ViewModel() {

    private val _cityGroups = MutableStateFlow<List<StateGroup>>(emptyList())
    val cityGroups: StateFlow<List<StateGroup>> get() = _cityGroups

    init {
        loadCityData()
    }

    private fun loadCityData() {
        viewModelScope.launch {
            _cityGroups.value = withContext(Dispatchers.IO) {
                try {
                    val inputStream = context.assets.open("city.json")
                    val jsonString = inputStream.bufferedReader().use { it.readText() }
                    val cityList = parseJson(jsonString)

                    cityList.groupBy { it.state }
                        .map { (state, cities) ->
                            StateGroup(state, cities.sortedBy { it.city }, false)
                        }
                        .sortedBy { it.state }
                } catch (e: Exception) {
                    e.printStackTrace()
                    emptyList()
                }
            }
        }
    }

    private fun parseJson(jsonString: String): List<City> {
        val cityList = mutableListOf<City>()
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            cityList.add(
                City(
                    city = obj.getString("city"),
                    state = obj.getString("admin_name"),
                    country = obj.getString("country"),
                    population = obj.getInt("population")
                )
            )
        }
        return cityList
    }

    fun toggleStateExpanded(stateName: String) {
        _cityGroups.value = _cityGroups.value.map {
            if (it.state == stateName) it.copy(isExpanded = !it.isExpanded) else it
        }
    }

    fun reverseOrder() {
        _cityGroups.value = _cityGroups.value.reversed()
    }
}
