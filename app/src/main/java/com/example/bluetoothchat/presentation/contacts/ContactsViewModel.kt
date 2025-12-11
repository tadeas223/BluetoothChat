package com.example.bluetoothchat.presentation.contacts

import androidx.lifecycle.ViewModel
import com.example.bluetoothchat.data.user.contact.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    contactRepository: ContactRepository
): ViewModel() {
    val contacts = contactRepository.selectAll()

    fun createUser() {

    }
}