package com.example.week3_assignment.data

data class SmartDevice(
    val id: Int,
    val name: String,
    val category: String,
    val room: String,
    val isOnline: Boolean,
    val isActive: Boolean,
    val powerConsumption: Double
)