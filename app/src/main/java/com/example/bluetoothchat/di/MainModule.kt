package com.example.bluetoothchat.di

import android.content.Context
import com.example.bluetoothchat.data.AndroidPermissionChecker
import com.example.bluetoothchat.domain.PermissionChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun providePermissionChecker(@ApplicationContext context: Context): PermissionChecker {
        return AndroidPermissionChecker(context)
    }
}