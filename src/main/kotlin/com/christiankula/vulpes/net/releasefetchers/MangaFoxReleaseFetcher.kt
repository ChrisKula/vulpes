package com.christiankula.vulpes.net.releasefetchers

import com.christiankula.vulpes.manga.Chapter
import com.christiankula.vulpes.manga.Manga
import com.christiankula.vulpes.net.connection.ConnectionFactory
import com.christiankula.vulpes.utils.StringUtils
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import java.io.IOException
import java.util.regex.Pattern

class MangaFoxReleaseFetcher : ReleaseFetcher() {
    private val VOLUME_NOT_AVAILABLE = "NA"
    private val CHAPTER_NOT_AVAILABLE = "NA"

    private val BASE_MANGAFOX_URL = "http://mangafox.me"
    private val BASE_MANGA_URL = "http://mangafox.me/manga/%s"
    private val BASE_RSS_URL = "http://mangafox.me/rss/%s.xml"

    private val JSOUP_HTML_CONNECTION = ConnectionFactory.createJsoupConnection(BASE_MANGAFOX_URL, Parser.htmlParser())
    private val JSOUP_XML_CONNECTION = ConnectionFactory.createJsoupConnection(BASE_MANGAFOX_URL, Parser.xmlParser())

    override fun fetchReleases(manga: Manga): Manga {
        val updatedManga = manga.copy(name = StringUtils.clean(manga.name.capitalize()),
                url = String.format(BASE_MANGA_URL, transformToMangaFoxRssName(manga.name)))

        val rssUrl = String.format(BASE_RSS_URL, transformToMangaFoxRssName(manga.name))

        var rssRootElement: Element? = null

        try {
            rssRootElement = JSOUP_XML_CONNECTION.url(rssUrl).get().getElementsByTag("rss")[0]
        } catch (e: IOException) {
            System.err.println("[ERROR] Couldn't connect to mangafox.me. " +
                    "Check your Internet connection. Keep in mind that mangafox.me may be down.")
            System.exit(1)
        } catch (ioobe: IndexOutOfBoundsException) {
            System.err.println("[ERROR] The manga " + manga.name
                    + " does not exist (or it is refered differently by MangaFox)")
            System.exit(1)
        }

        val rssChapters = rssRootElement!!.getElementsByTag("item")

        rssChapters.forEach { element: Element ->
            val title = element.getElementsByTag("title")[0].ownText()

            var volumeNumber = VOLUME_NOT_AVAILABLE
            var chapterNumber = CHAPTER_NOT_AVAILABLE

            var pattern = Pattern.compile("Vol ([0-9.TBD]+)")
            var matcher = pattern.matcher(title)

            if (matcher.find()) {
                volumeNumber = matcher.group(1).replaceFirst("^0+(?!$)".toRegex(), "")
            }

            pattern = Pattern.compile("Ch ([0-9.]+)")
            matcher = pattern.matcher(title)

            if (matcher.find()) {
                chapterNumber = matcher.group(1).replaceFirst("^0+(?!$)".toRegex(), "")
            }

            var chapter = Chapter(volumeNumber, chapterNumber, 0, element.getElementsByTag("link")[0].ownText())

            if (!manga.chapters.contains(chapter)) {
                chapter = chapter.copy(pageCount = fetchPageCount(chapter))
                if (chapter.pageCount > 0) {
                    updatedManga.chapters.add(chapter)
                }
            }

            // Because of how MangaFox handles incomings requests incoming in a short period,
            // fetching page counts can't be parallelized and a small delay is necessary.
            // Same applies for page downloading, thus making MangaFox crawling very slow
            Thread.sleep(150)
        }

        return updatedManga
    }

    fun transformToMangaFoxRssName(mangaName: String): String {
        return StringUtils.stripAccents(mangaName).replace(" ".toRegex(), "_").replace("[^0-9a-zA-Z_]".toRegex(), "").toLowerCase()
    }

    private fun fetchPageCount(chapter: Chapter): Int {
        val chapterFirstPage: Document
        try {
            chapterFirstPage = JSOUP_HTML_CONNECTION.url(chapter.url).get()
        } catch (e1: IOException) {
            val error = "Couldn't retrieve the number of pages of this chapter : " + chapter.chapterNumber
            System.err.println("[ERROR] " + error)
            return -1
        }

        val e = chapterFirstPage.getElementsByTag("option")
        val pagesCount = e.size / 2 - 1

        if (pagesCount <= 0) {
            val error = "Couldn't retrieve the number of pages of this chapter : " + chapter.chapterNumber + ". Keep in mind in can be a problem on mangafox.me's side."
            println("[ERROR] " + error)
            return -1
        } else {
            return pagesCount
        }
    }
}