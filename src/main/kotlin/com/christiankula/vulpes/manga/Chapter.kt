package com.christiankula.vulpes.manga

data class Chapter(val associatedVolume: String, val chapterNumber: String, val pageCount: Int, val url: String) : Comparable<Chapter> {
    val VOLUME_NOT_AVAILABLE = "NA"
    val VOLUME_TO_BE_DETERMINED = "TBD"

    private fun getVolumeValue(volume: String): Double {
        when (volume) {
            VOLUME_NOT_AVAILABLE -> {
                return Double.MAX_VALUE
            }
            VOLUME_TO_BE_DETERMINED -> {
                return Double.MAX_VALUE - 1
            }
            else -> {
                return volume.toDouble()
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
}
