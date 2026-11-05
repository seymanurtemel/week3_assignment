package com.example.week3_assignment.viewmodel

import androidx.lifecycle.ViewModel
import com.example.week3_assignment.data.DataSource
import com.example.week3_assignment.data.SmartDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val allDevices = DataSource.devices

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedRoom = MutableStateFlow<String?>(null)
    val selectedRoom: StateFlow<String?> = _selectedRoom.asStateFlow()

    private val _filteredDevices = MutableStateFlow(allDevices.toList())
    val filteredDevices: StateFlow<List<SmartDevice>> = _filteredDevices.asStateFlow()

    private val _totalActivePower = MutableStateFlow(calculateTotalPower(allDevices))
    val totalActivePower: StateFlow<Double> = _totalActivePower.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun onRoomSelected(room: String?) {
        _selectedRoom.value = room
        applyFilters()
    }

    fun toggleDeviceStatus(deviceId: Int) {
        val index = allDevices.indexOfFirst { it.id == deviceId }
        if (index != -1) {
            val device = allDevices[index]
            allDevices[index] = device.copy(isActive = !device.isActive)
            applyFilters()
            _totalActivePower.value = calculateTotalPower(allDevices)
        }
    }

    private fun applyFilters() {
        val query = _searchQuery.value.trim().lowercase()
        val room = _selectedRoom.value

        val filtered = allDevices.filter { device ->
            val matchesSearch = query.isEmpty() || device.name.lowercase().contains(query)
            val matchesRoom = room == null || room == "Tümü" || device.room == room
            matchesSearch && matchesRoom
        }
        _filteredDevices.value = filtered
    }

    private fun calculateTotalPower(devices: List<SmartDevice>): Double {
        return devices.filter { it.isOnline && it.isActive }.sumOf { it.powerConsumption }
    }
}