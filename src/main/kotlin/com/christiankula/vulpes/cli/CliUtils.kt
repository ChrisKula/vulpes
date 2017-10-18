package com.christiankula.vulpes.cli

import com.beust.jcommander.JCommander
import com.christiankula.vulpes.log.Log

class CliUtils private constructor() {

    companion object {
        fun printErrorAndExit(exitCode: Int, errorMessage: String) {
            Log.e("$errorMessage (exit code $exitCode)")
            System.exit(exitCode)
        }

        fun printUsageAndExit(jCommander:JCommander) {
            jCommander.usage()
            System.exit(0)
        }

        fun printVersionAndExit() {
            //TODO Fetch version from build.gradle
            Log.p("Vulpes by Chris Kula v0.1.0")
            System.exit(0)
        }
    }
}