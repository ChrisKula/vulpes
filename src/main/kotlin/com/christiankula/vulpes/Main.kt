package com.christiankula.vulpes

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException
import com.christiankula.vulpes.cli.Cli
import com.christiankula.vulpes.cli.CliUtils
import com.christiankula.vulpes.cli.messages.Error
import com.christiankula.vulpes.cli.messages.Warning
import com.christiankula.vulpes.log.Log
import com.christiankula.vulpes.manga.MangaDownloadManager
import com.christiankula.vulpes.manga.models.Manga
import com.christiankula.vulpes.manga.models.Source
import com.christiankula.vulpes.quirks.MangaFoxQuirks
import com.christiankula.vulpes.utils.StringUtils

private val cli = Cli()

private val jCommander = JCommander.newBuilder()
        .addObject(cli)
        .programName(Cli.PROGRAM_NAME)
        .build()!!

fun main(args: Array<String>) {
    try {
        jCommander.parse(*args)
    } catch (e: ParameterException) {
        CliUtils.printErrorAndExit(Error.MAIN_INCORRECT_PARAMETERS)
    }

    if (cli.help) {
        CliUtils.printUsageAndExit(jCommander)
    }

    if (cli.version) {
        CliUtils.printVersionAndExit()
    }

    if (cli.chapter != null && cli.volume == null) {
        CliUtils.printErrorAndExit(Error.MAIN_VOLUME_REQUIRED_WHEN_DOWNLOADING_CHAPTER)
    }

    if (cli.source == null) {
        Log.w(Warning.MAIN_SOURCE_DEFAULTED_TO_MANGA_FOX.message)
        cli.source = Source.MANGA_FOX
    }

    if (cli.source == Source.MANGA_FOX) {
        MangaFoxQuirks.ignoreInvalidSslCertificates()
    }

    val mangaDownloadManager = MangaDownloadManager(Manga(name = StringUtils.clean(cli.mangaName!!.capitalize()), source = cli.source!!))

    if (cli.volume != null && cli.chapter != null) {
        mangaDownloadManager.downloadChapter(cli.volume!!, cli.chapter!!)
    } else if (cli.volume != null) {
        mangaDownloadManager.downloadVolume(cli.volume!!)
    } else {
        mangaDownloadManager.downloadManga()
    }
}
