package com.christiankula.vulpes.net.releasefetchers

import com.christiankula.vulpes.manga.models.Chapter
import com.christiankula.vulpes.manga.models.Manga
import com.christiankula.vulpes.net.connection.ConnectionFactory
import com.christiankula.vulpes.quirks.MangaFoxQuirks
import com.christiankula.vulpes.utils.StringUtils
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import java.io.IOException
import java.util.regex.Pattern

class MangaFoxReleaseFetcher : ReleaseFetcher() {
    private val VOLUME_NOT_AVAILABLE = "NA"
    private val CHAPTER_NOT_AVAILABLE = "NA"

    private val BASE_MANGAFOX_URL = "https://mangafox.me"
    private val BASE_MANGA_URL = "https://mangafox.me/manga/%s"
    private val BASE_RSS_URL = "https://mangafox.me/rss/%s.xml"

    private val JSOUP_HTML_CONNECTION = ConnectionFactory.newJsoupConnection(BASE_MANGAFOX_URL, Parser.htmlParser())
    private val JSOUP_XML_CONNECTION = ConnectionFactory.newJsoupConnection(BASE_MANGAFOX_URL, Parser.xmlParser())

    override fun fetchReleases(manga: Manga): Manga {
        val updatedManga = manga.copy(url = String.format(BASE_MANGA_URL,
                transformToMangaFoxRssName(manga.name)))

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
                    + " does not exist (or it is referred differently by MangaFox)")
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

            var chapterUrl = element.getElementsByTag("link")[0].ownText()

            if (chapterUrl.startsWith("//")) {
                chapterUrl = "https:" + chapterUrl
            }

            var chapter = Chapter(volumeNumber, chapterNumber, 0, chapterUrl)

            if (!manga.chapters.contains(chapter)) {
                chapter = chapter.copy(pageCount = fetchPageCount(chapter))
                if (chapter.pageCount > 0) {
                    updatedManga.chapters.add(chapter)
                }
            }
        }

        return updatedManga
    }

    private fun transformToMangaFoxRssName(mangaName: String): String {
        return StringUtils.stripAccents(mangaName).replace(" ".toRegex(), "_").toLowerCase()
    }

    private fun fetchPageCount(chapter: Chapter): Int {
        val chapterFirstPage: Document
        var pageCount = -1

        try {
            chapterFirstPage = JSOUP_HTML_CONNECTION.url(chapter.url).get()
            pageCount = chapterFirstPage.getElementsByTag("option").size / 2 - 1

            if (pageCount <= 0) {
                System.err.println("[ERROR] Couldn't retrieve the page count for chapter ${chapter.chapterNumber}")
                pageCount = -1
            }
        } catch (ioe: IOException) {
            System.err.println("[ERROR] Couldn't retrieve the page count for chapter ${chapter.chapterNumber} (${ioe.localizedMessage})")
        } finally {
            Thread.sleep(MangaFoxQuirks.DELAY_BETWEEN_HTTP_REQUESTS_MS)
            return pageCount
        }
    }
}