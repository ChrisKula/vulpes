package com.christiankula.vulpes.cli.messages

enum class Warning(val message: String) {
    MAIN_SOURCE_DEFAULTED_TO_MANGA_FOX("No source specified, defaulted to MangaFox"),

    JSON_MANAGER_INVALID_JSON_CREATE_NEW_ONE("JSON file is not correctly structured. A new one will be created.")
}
