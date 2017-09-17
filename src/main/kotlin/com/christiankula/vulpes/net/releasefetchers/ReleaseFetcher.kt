package com.christiankula.vulpes.net.releasefetchers

import com.christiankula.vulpes.manga.models.Manga
import com.christiankula.vulpes.manga.models.Source

abstract class ReleaseFetcher {

    /**
     * Fetch all releases for the specified manga according to its source. Also initializes its URL field.
     * @return the given manga with its chapters and URL updated
     */
    abstract fun fetchReleases(manga: Manga): Manga

    companion object {
        fun fromSource(source: Source): ReleaseFetcher {

            when (source) {
                Source.JAPSCAN -> {
                    throw NotImplementedError("No release fetcher implemented for Japscan source")
                }
                else -> {
                    return MangaFoxReleaseFetcher()
                }
            }
        }
    }
}