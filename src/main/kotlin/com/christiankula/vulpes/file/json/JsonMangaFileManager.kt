package com.christiankula.vulpes.file.json

import com.christiankula.vulpes.manga.models.Manga
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class JsonMangaFileManager private constructor() {
    companion object {
        private fun getMangaJsonFile(manga: Manga): File {
            return File("${manga.name}${File.separator}${manga.name.toLowerCase()}.json")
        }

        fun mangaFromJson(manga: Manga): Manga? {
            val mangaJsonFile = getMangaJsonFile(manga)

            var mangaFromJson: Manga? = null

            if (mangaJsonFile.exists()) {
                try {
                    mangaFromJson = Gson().fromJson(mangaJsonFile.reader(), Manga::class.java)
                } catch (i: JsonIOException) {
                    System.err.println("[ERROR] Error while parsing the JSON file.")
                    System.exit(20)
                } catch (i: JsonSyntaxException) {
                    System.err.println("[WARN] JSON file is not correctly structured. A new one will be created.")
                }

                if (mangaFromJson != null) {
                    if (manga.source != mangaFromJson.source) {
                        System.err.println("[ERROR] The provided source is different than the one previously provided. "
                                + "Please continue downloading with " + manga.source
                                + " or start from scratch with another source.")
                        System.exit(21)
                    }
                }
            }

            return mangaFromJson
        }

        fun writeToJsonFile(manga: Manga) {
            try {
                FileUtils.writeStringToFile(getMangaJsonFile(manga), Gson().toJson(manga), Charset.forName("UTF-8"))
            } catch (e: IOException) {
                System.err.println("[ERROR] Unable to write to the JSON file.")
                System.err.println(e.message)
                System.exit(22)
            }

        }
    }
}
