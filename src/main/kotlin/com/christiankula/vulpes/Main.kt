package com.christiankula.vulpes
import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException

import com.christiankula.vulpes.cli.Cli
import com.christiankula.vulpes.manga.models.Source

val cli = Cli()

val jCommander = JCommander.newBuilder()
        .addObject(cli)
        .programName("java -jar vulpes.jar")
        .build()!!

fun main(args: Array<String>) {
    try {
        jCommander.parse(*args)
    } catch (e: ParameterException) {
        System.err.println("Incorrect parameters. Please use -h option to show usage")
        return
    }

    if (cli.help) {
        printUsageAndExit()
    }

    if (cli.version) {
        printVersionAndExit()
    }

    if (cli.source == null) {
        cli.source = Source.MANGA_FOX
    } else {
        //init fetcher
    }

    if (cli.volume != null && cli.chapter != null) {
        //download chapter
    } else if (cli.volume != null) {
        //download volume
    } else {
        //download whole manga
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