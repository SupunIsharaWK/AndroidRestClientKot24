package com.supunishara.restclientkot24.data_classes

data class Header(val name: String, val value: String) {

    override fun equals(other: Any?): Boolean {
        val header: Header = other as Header
        return this.name.equals(header.name, ignoreCase = true)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}