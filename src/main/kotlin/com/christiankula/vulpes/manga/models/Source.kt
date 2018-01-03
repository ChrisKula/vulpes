package com.christiankula.vulpes.manga.models

import com.christiankula.vulpes.cli.messages.Warning
import com.christiankula.vulpes.log.Log

enum class Source(val source: String) {
    MANGA_FOX("mf"),
    JAPSCAN("js");

    companion object {
        fun fromString(source: String?): Source {
            when (source) {
                "js" -> {
                    throw NotImplementedError()
                }
                else -> {
                    if (source != MANGA_FOX.source) {
                        Log.w(Warning.CLI_SOURCE_NOT_RECOGNIZED_DEFAULTED_TO_MANGA_FOX.message)
                    }
                    return MANGA_FOX
                }
            }
        }
    }
}