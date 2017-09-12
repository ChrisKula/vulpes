package com.christiankula.vulpes.net.downloaders

import com.christiankula.vulpes.manga.models.Chapter
import com.christiankula.vulpes.manga.models.Manga
import com.christiankula.vulpes.net.connection.ConnectionFactory
import org.jsoup.parser.Parser

class MangaFoxDownloader : Downloader() {
    override fun downloadChapters(manga: Manga, chapters: Set<Chapter>) {
        for (chapter in chapters) {
            downloadChapterIfNeeded(manga, chapter)
            Thread.sleep(200)
        }
    }

    override fun downloadChapter(manga: Manga, chapter: Chapter): Boolean {
        val pageUrlModel = getPageUrlModel(chapter.url)

        for (pageNumber in 1..chapter.pageCount) {
            val pageUrl = String.format(pageUrlModel, pageNumber)

            val page = ConnectionFactory.createJsoupConnection(pageUrl, Parser.htmlParser()).get()

            if (page.hasText()) {
                val imgUrl = page.getElementById("viewer").getElementsByTag("img")[0].attr("src")

                downloadImageToDisk(manga, chapter, imgUrl, pageNumber)

                Thread.sleep(200)
            }
        }

        return isChapterDownloaded(manga, chapter)
    }

    private fun getPageUrlModel(url: String): String {
        return url.replace("1.html", "%d.html")
    }
}