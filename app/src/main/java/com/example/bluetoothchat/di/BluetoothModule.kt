package com.example.bluetoothchat.di

import android.content.Context
import com.example.bluetoothchat.data.bluetooth.AndroidBluetoothConnectService
import com.example.bluetoothchat.data.bluetooth.AndroidBluetoothScanService
import com.example.bluetoothchat.domain.bluetooth.BluetoothConnectService
import com.example.bluetoothchat.domain.bluetooth.BluetoothScanService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BluetoothModule {
    @Provides
    @Singleton
    fun provideBluetoothScanService(@ApplicationContext context: Context): BluetoothScanService {
        return AndroidBluetoothScanService(context)
    }

    @Provides
    @Singleton
    fun provideBluetoothConnectService(@ApplicationContext context: Context): BluetoothConnectService {
        return AndroidBluetoothConnectService(context)
    }

}