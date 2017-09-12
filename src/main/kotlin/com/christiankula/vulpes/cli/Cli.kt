package com.christiankula.vulpes.cli

import com.beust.jcommander.IStringConverter
import com.beust.jcommander.Parameter
import com.christiankula.vulpes.manga.models.Source


class Cli {
    @Parameter(description = "Name of the manga to download")
    var mangaName: String? = null

    @Parameter(names = arrayOf("--volume", "-v"), description = "Specify the volume to " +
            "download. Accepted values : 0..* | TBD | NA", order = 1)
    var volume: String? = null

    @Parameter(names = arrayOf("--chapter", "-c"), description = "Specify the chapter to " +
            "download. Must be used with the -v option because some mangas have multiple chapters " +
            "with the same number. Accepted values 0..* | TBD | NA", order = 2)
    var chapter: String? = null

    @Parameter(names = arrayOf("--source", "-s"), description = "Specify the source to " +
            "download from.", converter = SourceConverter::class, order = 3)
    var source: Source? = null

    @Parameter(names = arrayOf("--version", "-V"), description = "Display Vulpes version",
            order = 4)
    var version = false

    @Parameter(names = arrayOf("--help", "-h"), help = true, description = "Display help " +
            "informations about Vulpes", order = 5)
    var help: Boolean = false

    class SourceConverter : IStringConverter<Source> {
        override fun convert(value: String?): Source {
            return Source.fromString(value)
        }

    }

    override fun toString(): String {
        return "Cli(mangaName=$mangaName, volume=$volume, chapter=$chapter, source=$source, version=$version, help=$help)"
    }
}

