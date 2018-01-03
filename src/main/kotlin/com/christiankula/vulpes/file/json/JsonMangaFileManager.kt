package com.christiankula.vulpes.file.json

import com.christiankula.vulpes.cli.CliUtils
import com.christiankula.vulpes.cli.messages.Error
import com.christiankula.vulpes.cli.messages.Warning
import com.christiankula.vulpes.log.Log
import com.christiankula.vulpes.manga.models.Manga
import com.google.gson.GsonBuilder
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class JsonMangaFileManager private constructor() {
    companion object {
        private val gson = GsonBuilder().setPrettyPrinting().create()

        private fun getMangaJsonFile(manga: Manga): File {
            return File("${manga.name}${File.separator}${manga.name.toLowerCase()}.json")
        }

        fun mangaFromJson(manga: Manga): Manga? {
            val mangaJsonFile = getMangaJsonFile(manga)

            var mangaFromJson: Manga? = null

            if (mangaJsonFile.exists()) {
                try {
                    mangaFromJson = gson.fromJson(mangaJsonFile.reader(), Manga::class.java)
                } catch (jioe: JsonIOException) {
                    CliUtils.printErrorAndExit(Error.JSON_MANAGER_ERROR_WHILE_READING_JSON_FILE)
                } catch (jse: JsonSyntaxException) {
                    Log.w(Warning.JSON_MANAGER_INVALID_JSON_FILE_CREATE_NEW_ONE.message)
                }

                if (mangaFromJson != null) {
                    if (manga.source != mangaFromJson.source) {
                        val error = Error.JSON_MANAGER_PROVIDED_SOURCE_DIFFERENT_THAN_PREVIOUSLY_PROVIDED
                        CliUtils.printErrorAndExit(error.code, String.format(error.message, manga.source))
                    }
                }
            }

            return mangaFromJson
        }

        fun writeToJsonFile(manga: Manga) {
            try {
                FileUtils.writeStringToFile(getMangaJsonFile(manga), gson.toJson(manga), Charset.forName("UTF-8"))
            } catch (ioe: IOException) {
                val error = Error.JSON_MANAGER_UNABLE_TO_WRITE_TO_JSON_FILE
                CliUtils.printErrorAndExit(error.code, String.format(error.message, ioe.message))
            }
        }
    }
}
