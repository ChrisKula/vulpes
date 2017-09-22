package com.christiankula.vulpes.net.download

import com.christiankula.vulpes.file.filters.ImageFileFilter
import com.christiankula.vulpes.manga.models.Chapter
import com.christiankula.vulpes.manga.models.Manga
import com.christiankula.vulpes.manga.models.Source
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private const val VOLUME_FOLDER = "v_"
private const val CHAPTER_FOLDER = "ch_"

private const val TIMEOUT_IN_MILLIS = 5000

abstract class Downloader {

    private val THREAD_POOL_SIZE: Int = Runtime.getRuntime().availableProcessors() - 1
    private val executorService: ExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE)

    companion object {
        fun fromSource(source: Source): Downloader {

            when (source) {
                Source.JAPSCAN -> {
                    throw NotImplementedError("No release fetcher implemented for Japscan source")
                }
                else -> {
                    return MangaFoxDownloader()
                }
            }
        }
    }

    fun downloadManga(manga: Manga): Boolean {
        val chaptersToDownload = getChaptersToDownload(manga)

        return if (chaptersToDownload.isEmpty()) {
            System.out.println("[UP TO DATE] The manga " + manga.name.toUpperCase() + " is up to date")
            false
        } else {
            downloadChapters(manga, chaptersToDownload)
            true
        }
    }

    fun downloadVolume(manga: Manga, volumeNumber: String): Boolean {
        val chapters = TreeSet<Chapter>()

        manga.chapters.forEach { chapter ->
            if (chapter.associatedVolume.equals(volumeNumber, true)) {
                chapters.add(chapter)
            }
        }

        return if (chapters.isEmpty()) {
            System.err.println("[ERR] Volume $volumeNumber not downloaded : no found for the manga ${manga.name}")
            false
        } else {
            downloadChapters(manga, chapters)
            true
        }
    }

    fun downloadChapter(manga: Manga, volumeNumber: String, chapterNumber: String): Boolean {
        manga.chapters.forEach { chapter ->
            if (chapter.associatedVolume.equals(volumeNumber, true) && chapter.chapterNumber.equals(chapterNumber, true)) {
                val chapters = TreeSet<Chapter>()
                chapters.add(chapter)

                downloadChapters(manga, chapters)
                return true
            }
        }

        System.err.println("[ERR] Chapter $chapterNumber (Volume $volumeNumber) not downloaded : not found for the manga ${manga.name}")
        return false
    }

    protected open fun downloadChapters(manga: Manga, chapters: Set<Chapter>) {
        for (chapter in chapters) {
            this.executorService.execute({
                downloadChapterIfNeeded(manga, chapter)
            })
        }

        this.executorService.shutdown()

        while (!this.executorService.isTerminated) {
        }
    }

    protected fun downloadChapterIfNeeded(manga: Manga, chapter: Chapter) {
        if (!isChapterDownloaded(manga, chapter)) {
            downloadChapter(manga, chapter)
        } else {
            println("[SKIP] Skipping Chapter ${chapter.chapterNumber} (Volume ${chapter.associatedVolume}) : Already downloaded")
        }
    }

    protected abstract fun downloadChapter(manga: Manga, chapter: Chapter): Boolean

    private fun getChapterDirectory(mangaName: String, chapter: Chapter): File {
        return File(mangaName + File.separator + VOLUME_FOLDER + chapter.associatedVolume + File.separator + CHAPTER_FOLDER + chapter.chapterNumber + File.separator)
    }

    private fun getChaptersToDownload(manga: Manga): Set<Chapter> {
        val chaptersToUpdate = TreeSet<Chapter>()

        manga.chapters.forEach { chapter ->
            if (!isChapterDownloaded(manga, chapter)) {
                chaptersToUpdate.add(chapter)
            }
        }

        return chaptersToUpdate
    }


    protected fun downloadImageToDisk(manga: Manga, chapter: Chapter, imgUrl: String, pageNumber: Int): Boolean {
        val file = File("${getChapterDirectory(manga.name, chapter)}/0$pageNumber.jpg")

        try {
            if (file.exists()) {
                file.delete()
            }
            FileUtils.copyURLToFile(URL(imgUrl), file, TIMEOUT_IN_MILLIS, TIMEOUT_IN_MILLIS)
        } catch (ioe: IOException) {
            file.delete()
        }

        return file.exists()
    }

    protected fun isChapterDownloaded(manga: Manga, chapter: Chapter): Boolean {
        val chapterDirectory = getChapterDirectory(manga.name, chapter)

        return chapterDirectory.exists() && chapterDirectory.listFiles(ImageFileFilter()).size == chapter.pageCount
    }
}