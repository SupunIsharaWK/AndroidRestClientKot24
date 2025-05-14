package com.supunishara.restclientkot24.data_classes

data class CacheData(
    var url: String = "",
    var method: String = "",
    var data: String = "",
    var createdAt: Long = 0L,
    var updatedAt: Long = 0L,
    var timeout: Int = 0,
    var status: Boolean = false,
)

