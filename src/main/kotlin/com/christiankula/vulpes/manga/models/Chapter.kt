package com.christiankula.vulpes.manga.models

import com.christiankula.vulpes.manga.VOLUME_NOT_AVAILABLE
import com.christiankula.vulpes.manga.VOLUME_TO_BE_DETERMINED
import com.google.gson.annotations.SerializedName


data class Chapter(@SerializedName("volume") val associatedVolume: String, @SerializedName("chapter") val chapterNumber: String, val pageCount: Int, val url: String) : Comparable<Chapter> {

    private fun getVolumeValue(volume: String): Double {
        return when (volume) {
            VOLUME_NOT_AVAILABLE -> {
                Double.MAX_VALUE
            }
            VOLUME_TO_BE_DETERMINED -> {
                Double.MAX_VALUE - 1
            }
            else -> {
                volume.toDouble()
            }
        }
    }

    override fun compareTo(other: Chapter): Int {
        val value = getVolumeValue(associatedVolume)
        val otherValue = getVolumeValue(other.associatedVolume)

        var diff = value.compareTo(otherValue)

        if (diff == 0) {
            diff = chapterNumber.toDouble().compareTo(other.chapterNumber.toDouble())
        }

        return diff
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null) {
            return false
        }

        val otherChapter = other as Chapter

        if (associatedVolume != otherChapter.associatedVolume) {
            return false
        }

        if (chapterNumber != otherChapter.chapterNumber) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + associatedVolume.hashCode()
        result = prime * result + chapterNumber.hashCode()
        return result
    }
}
