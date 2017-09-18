package com.christiankula.vulpes.manga

import com.christiankula.vulpes.file.json.JsonMangaFileManager
import com.christiankula.vulpes.manga.models.Manga
import com.christiankula.vulpes.net.downloaders.Downloader
import com.christiankula.vulpes.net.releasefetchers.ReleaseFetcher

class MangaDownloadManager(var manga: Manga) {
    private val releaseFetcher: ReleaseFetcher = ReleaseFetcher.fromSource(manga.source)
    private val downloader: Downloader = Downloader.fromSource(manga.source)

    init {
        val mangaFromJson = JsonMangaFileManager.mangaFromJson(manga)

        if (mangaFromJson != null) {
            manga = releaseFetcher.fetchReleases(mangaFromJson)
        } else {
            manga = releaseFetcher.fetchReleases(manga)
        }

        JsonMangaFileManager.writeToJsonFile(manga)
    }

    fun downloadChapter(volume: String, chapterNumber: String) {
        downloader.downloadChapter(manga, volume, chapterNumber)
    }

    fun downloadVolume(volume: String) {
        downloader.downloadVolume(manga, volume)
    }

    fun downloadManga() {
        downloader.downloadManga(manga)
    }
}