package com.example.bluetoothchat.di

import android.content.Context
import com.example.bluetoothchat.data.bluetooth.AndroidBluetoothScanner
import com.example.bluetoothchat.data.bluetooth.BluetoothScanner
import dagger.Binds
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
    fun provideBluetoothScanner(@ApplicationContext context: Context): BluetoothScanner {
        return AndroidBluetoothScanner(context)
    }

}