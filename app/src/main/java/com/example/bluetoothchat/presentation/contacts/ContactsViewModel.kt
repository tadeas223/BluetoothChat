package com.example.bluetoothchat.presentation.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bluetoothchat.domain.user.contact.Contact
import com.example.bluetoothchat.domain.user.contact.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    contactRepository: ContactRepository
): ViewModel() {
    var contacts = contactRepository.selectAll()

    fun createUser() {

    }
}