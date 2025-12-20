package com.example.bluetoothchat.domain

interface PermissionChecker {
    fun hasAll(permissions: List<String>): Boolean
    fun getUnsatisfied(permissions: List<String>): List<String>
    fun checkOrThrow(permissions: List<String>)
}