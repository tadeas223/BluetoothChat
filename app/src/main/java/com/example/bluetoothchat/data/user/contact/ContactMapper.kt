package com.example.bluetoothchat.data.user.contact

import com.example.bluetoothchat.domain.user.contact.Contact

fun ContactEntity.toContact(): Contact {
    return Contact(
        id = this.id,
        username = this.username,
        address = this.address,
    )
}

fun Contact.toEntity(): ContactEntity {
    return ContactEntity(
        id = this.id,
        username = this.username,
        address = this.address,
    )
}
