package com.example.bluetoothchat.data

import android.content.Context
import android.content.pm.PackageManager
import com.example.bluetoothchat.domain.PermissionChecker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidPermissionChecker @Inject constructor(
    @ApplicationContext val context: Context,
): PermissionChecker {

    override fun getUnsatisfied(permissions: List<String>): List<String> {
        val unsatisfied = mutableListOf<String>()
        permissions.forEach { perm ->
            if(hasPermission(context, perm)) {
                unsatisfied.add(perm)
            }
        }

        return unsatisfied.toList()
    }

    override fun hasAll(permissions: List<String>): Boolean {
        return hasPermissions(context, permissions)
    }

    override fun checkOrThrow(permissions: List<String>) {
        if(!hasAll(permissions)) {
            throw SecurityException("missing permissions")
        }
    }


}