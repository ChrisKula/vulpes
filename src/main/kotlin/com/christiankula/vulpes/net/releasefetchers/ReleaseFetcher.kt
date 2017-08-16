package com.christiankula.vulpes.net.releasefetchers

import com.christiankula.vulpes.manga.Manga

abstract class ReleaseFetcher {

    /**
     * Fetch all releases for the specified manga according to its source. Also initializes its URL field.
     * @return the given manga with its chapters and URL updated
     */
    abstract fun fetchReleases(manga: Manga): Manga
}