package com.poc.expandablelist.data
data class City(
    val city: String,
    val state: String,
    val country: String,
    val population: Int
)
data class StateGroup(
    val state: String,
    val cities: List<City>,
    val isExpanded: Boolean
)
