package com.supunishara.restclientkot24.model

/**
 * Represents a file part for a multipart/form-data HTTP request.
 *
 * @param name The form field name.
 * @param filename The name of the file being uploaded.
 * @param mimeType The MIME type of the file.
 * @param fileContent The raw file content as a ByteArray.
 */
data class MultipartFileData(
    val name: String,
    val filename: String,
    val mimeType: String,
    val fileContent: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MultipartFileData

        if (name != other.name) return false
        if (filename != other.filename) return false
        if (mimeType != other.mimeType) return false
        if (!fileContent.contentEquals(other.fileContent)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + filename.hashCode()
        result = 31 * result + mimeType.hashCode()
        result = 31 * result + fileContent.contentHashCode()
        return result
    }
}