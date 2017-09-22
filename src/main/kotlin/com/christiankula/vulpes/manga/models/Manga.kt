package com.christiankula.vulpes.manga.models

import java.util.*

data class Manga(val name: String, val url: String = "", val source: Source, val chapters: MutableSet<Chapter> = TreeSet()) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is Manga) {
            return false
        }

        if (url == other.url && source == other.source) {
            if (chapters.size == other.chapters.size) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + source.hashCode()
        result = 31 * result + chapters.hashCode()
        return result
    }
}