package com.picpay.desafio.android.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.picpay.desafio.android.domain.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    @SerializedName("img") val img: String,
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String
) : Parcelable

fun List<UserResponse>.toDomain(): List<User> {
    return this.map {
        User(
            img = it.img,
            name = it.name,
            id = it.id,
            username = it.username
        )
    }
}
