package com.christiankula.vulpes.net.releasefetchers

import com.christiankula.vulpes.manga.Manga
import com.christiankula.vulpes.manga.Source
import org.junit.Test

class MangaFoxReleaseFetcherTests {

    val mangaFoxReleaseFetcher = MangaFoxReleaseFetcher()

    @Test
    fun HorimiyaFeedShouldHaveAtLeast92Chapters() {
        //as of 11/08/17
        val initManga = Manga("horimiya", "http://mangafox.me/manga/horimiya/", Source.MANGA_FOX)


        val manga = mangaFoxReleaseFetcher.fetchReleases(initManga)

        assert(manga.chapters.size >= 92)
    }
}