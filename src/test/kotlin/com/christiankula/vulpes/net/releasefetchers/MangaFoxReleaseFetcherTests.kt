package com.christiankula.vulpes.net.releasefetchers

import com.christiankula.vulpes.manga.Manga
import com.christiankula.vulpes.manga.Source
import org.junit.Assert
import org.junit.Test

class MangaFoxReleaseFetcherTests {

    private val mangaFoxReleaseFetcher = MangaFoxReleaseFetcher()

    @Test
    fun HorimiyaFeedShouldHaveAtLeast92Chapters() {
        //as of 11/08/17
        val targetMinChapterCount = 92

        val initManga = Manga("horimiya", "http://mangafox.me/manga/horimiya/", Source.MANGA_FOX)


        val manga = mangaFoxReleaseFetcher.fetchReleases(initManga)

        Assert.assertEquals("Chapters counts don't match : should be $targetMinChapterCount, is ${manga.chapters.size} instead",true, manga.chapters.size >= targetMinChapterCount)
    }
}