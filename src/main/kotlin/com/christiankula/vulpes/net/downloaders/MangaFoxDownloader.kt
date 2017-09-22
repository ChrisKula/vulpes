package com.christiankula.vulpes.net.downloaders

import com.christiankula.vulpes.manga.models.Chapter
import com.christiankula.vulpes.manga.models.Manga
import com.christiankula.vulpes.net.connection.ConnectionFactory
import com.christiankula.vulpes.quirks.MangaFoxQuirks
import org.jsoup.parser.Parser

class MangaFoxDownloader : Downloader() {
    override fun downloadChapters(manga: Manga, chapters: Set<Chapter>) {
        for (chapter in chapters) {
            downloadChapterIfNeeded(manga, chapter)
            Thread.sleep(MangaFoxQuirks.DELAY_BETWEEN_HTTP_REQUESTS_MS)
        }
    }

    override fun downloadChapter(manga: Manga, chapter: Chapter): Boolean {
        val pageUrlModel = getPageUrlModel(chapter.url)

        for (pageNumber in 1..chapter.pageCount) {
            val pageUrl = String.format(pageUrlModel, pageNumber)

            val page = ConnectionFactory.newJsoupConnection(pageUrl, Parser.htmlParser()).get()

            if (page.hasText()) {
                val imgUrl = page.getElementById("viewer").getElementsByTag("img")[0].attr("src")

                downloadImageToDisk(manga, chapter, imgUrl, pageNumber)

                Thread.sleep(MangaFoxQuirks.DELAY_BETWEEN_HTTP_REQUESTS_MS)
            }
        }

        return isChapterDownloaded(manga, chapter)
    }

    private fun getPageUrlModel(url: String): String {
        return url.replace("1.html", "%d.html")
    }
}