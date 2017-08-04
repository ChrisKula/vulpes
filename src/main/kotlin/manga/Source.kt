package manga

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
                        println("Didn't recognize source, defaulted to MangaFox")
                    }
                    return MANGA_FOX
                }
            }
        }
    }
}