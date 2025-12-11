package com.example.bluetoothchat.data.bluetooth

import android.content.Context
import android.content.pm.PackageManager

fun hasPermissions(context: Context, permission: String): Boolean {
    return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
}
