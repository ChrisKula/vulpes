package com.christiankula.vulpes

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException
import com.christiankula.vulpes.cli.Cli
import com.christiankula.vulpes.manga.MangaDownloadManager
import com.christiankula.vulpes.manga.models.Manga
import com.christiankula.vulpes.manga.models.Source
import com.christiankula.vulpes.utils.StringUtils

val cli = Cli()

val jCommander = JCommander.newBuilder()
        .addObject(cli)
        .programName("java -jar vulpes.jar")
        .build()!!

fun main(args: Array<String>) {
    try {
        jCommander.parse(*args)
    } catch (e: ParameterException) {
        System.err.println("[ERROR] Incorrect parameters. Please use -h option to show usage")
        System.exit(10)
    }

    if (cli.help) {
        printUsageAndExit()
    }

    if (cli.version) {
        printVersionAndExit()
    }

    if (cli.source == null) {
        println("[INFO] No source specified, defaulted to MangaFox\n")
        cli.source = Source.MANGA_FOX
    }

    if (cli.chapter != null && cli.volume == null) {
        System.err.println("[ERROR] When specifying a chapter, you must also specify a volume as some " +
                "mangas have multiple chapters with the same number, e.g. 'Detective Conan'.")

        System.exit(11)
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


fun printUsageAndExit() {
    jCommander.usage()
    System.exit(0)
}

fun printVersionAndExit() {
    //TODO Fetch version from build.gradle
    println("Vulpes by Chris Kula v0.1.0")
    System.exit(0)
}