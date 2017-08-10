package manga

import java.util.*

data class Manga(val name: String, val url: String, val source: Source, val chapters: Set<Chapter> = TreeSet())