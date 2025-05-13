package com.supunishara.restclientkot24

class Result {
    public val UNKNOWN: Int = 0
    public val GENRIC_EXCEPTION: Int = -1
    public val OKHTTP_LIB_MISSING: Int = -2
    public val REQUIRED_PERMISSION_MISSING: Int = -3
    public val NOT_CONNECTED_TO_INTERNET: Int = -4
    public val INVALID_PARAMETERS: Int = -5
    public val SOCKET_TIMEOUT: Int = -6
    public val UNKNOWN_HOST: Int = -7
    public val MALFORMED_URL: Int = -8
    public val CONNECTION_EXCEPTION: Int by lazy { -9 }
    public val SOCKET_EXCEPTION: Int by lazy { -10 }
}