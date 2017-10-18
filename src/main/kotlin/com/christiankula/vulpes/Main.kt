package com.christiankula.vulpes

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException
import com.christiankula.vulpes.cli.Cli
import com.christiankula.vulpes.cli.CliUtils
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
        CliUtils.printErrorAndExit(10, "Incorrect parameters. Please use -h option to show usage")
    }

    if (cli.help) {
        CliUtils.printUsageAndExit(jCommander)
    }

    if (cli.version) {
        CliUtils.printVersionAndExit()
    }

    if (cli.chapter != null && cli.volume == null) {
        CliUtils.printErrorAndExit(11, "When specifying a chapter, you must also specify a volume as " +
                "some mangas have multiple chapters with the same number, e.g. 'Detective Conan'.")
    }

    if (cli.source == null) {
        Log.w("No source specified, defaulted to MangaFox")
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
