package com.example.week3_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.week3_assignment.data.SmartDevice
import com.example.week3_assignment.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SmartHomeMainScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun SmartHomeMainScreen(viewModel: HomeViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedRoom by viewModel.selectedRoom.collectAsState()
    val filteredDevices by viewModel.filteredDevices.collectAsState()
    val totalPower by viewModel.totalActivePower.collectAsState()

    val rooms = listOf("Tümü", "Salon", "Bahçe", "Antre", "Yatak Odası", "Mutfak", "Koridor", "Balkon", "Ofis")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 1. Canlı Enerji Sayacı Kartı
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Aktif Toplam Enerji Tüketimi", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$totalPower Watt",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // 2. Arama Çubuğu
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            placeholder = { Text("Cihaz adı ara...") },
            singleLine = true
        )

        // 3. Oda Filtreleme Sekmeleri
        ScrollableTabRow(
            selectedTabIndex = rooms.indexOf(selectedRoom ?: "Tümü").coerceAtLeast(0),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            edgePadding = 0.dp
        ) {
            rooms.forEach { room ->
                Tab(
                    selected = (selectedRoom == room) || (selectedRoom == null && room == "Tümü"),
                    onClick = {
                        val filterValue = if (room == "Tümü") null else room
                        viewModel.onRoomSelected(filterValue)
                    },
                    text = { Text(room) }
                )
            }
        }

        // 4. Cihaz Listesi (LazyColumn)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredDevices, key = { it.id }) { device ->
                DeviceItem(
                    device = device,
                    onToggle = { viewModel.toggleDeviceStatus(device.id) }
                )
            }
        }
    }
}

@Composable
fun DeviceItem(device: SmartDevice, onToggle: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = device.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Oda: ${device.room} | Kategori: ${device.category}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Tüketim: ${device.powerConsumption}W", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = if (device.isOnline) "Online" else "Offline",
                    color = if (device.isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = onToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (device.isActive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = if (device.isActive) "Kapat" else "Aç")
            }
        }
    }
}