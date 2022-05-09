package com.example.rindikapp.utils

import com.example.rindikapp.utils.Status.*

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(message: String): Resource<T> {
            return Resource(ERROR, null, message)
        }

        fun <T> loading(): Resource<T> {
            return Resource(LOADING, null, null)
        }
    }
}
