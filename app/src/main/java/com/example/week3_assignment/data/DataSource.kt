package com.example.week3_assignment.data

object DataSource {
    val devices = mutableListOf(
        SmartDevice(1, "Köşe Lambası", "LIGHT", "Salon", true, true, 15.0),
        SmartDevice(2, "Tavan Avizesi", "LIGHT", "Salon", true, false, 60.0),
        SmartDevice(3, "Bahçe Kamerası", "CAMERA", "Bahçe", true, true, 8.5),
        SmartDevice(4, "Giriş Kamerası", "CAMERA", "Antre", true, true, 8.5),
        SmartDevice(5, "Merkezi Termostat", "THERMOSTAT", "Salon", true, true, 12.0),
        SmartDevice(6, "Yatak Odası Kliması", "THERMOSTAT", "Yatak Odası", true, false, 1200.0),
        SmartDevice(7, "Mutfak Spotları", "LIGHT", "Mutfak", true, false, 45.0),
        SmartDevice(8, "Roborock S7", "VACUUM", "Koridor", true, true, 50.0),
        SmartDevice(9, "Balkon Aydınlatması", "LIGHT", "Balkon", false, false, 25.0),
        SmartDevice(10, "Çalışma Odası Lambası", "LIGHT", "Ofis", true, true, 12.0)
    )
}