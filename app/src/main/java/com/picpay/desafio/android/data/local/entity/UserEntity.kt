package com.picpay.desafio.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.picpay.desafio.android.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val username: String,
    val img: String
)


fun List<UserEntity>.toDomain() : List<User> {
    return this.map {
        User(
            id = it.id,
            name = it.name,
            username = it.username,
            img = it.img
        )
    }
}

fun List<User>.toEntity() : List<UserEntity> {
    return this.map {
        UserEntity(
            id = it.id,
            name = it.name,
            username = it.username,
            img = it.img
        )
    }
}

