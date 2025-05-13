package com.supunishara.restclientkot24.data_classes

data class CacheData(
    var url: String,
    var method: String,
    var data: String,
    var createdAt: Long,
    var updatedAt: Long,
    var timeout: Int,
    var status: Boolean,
)

