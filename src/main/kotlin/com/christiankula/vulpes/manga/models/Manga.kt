package com.christiankula.vulpes.manga.models

import java.util.*

data class Manga(val name: String, val url: String = "", val source: Source, val chapters: MutableSet<Chapter> = TreeSet())