package com.christiankula.vulpes.cli.messages

enum class Error(val code: Int, val message: String) {
    MAIN_INCORRECT_PARAMETERS(10, "Incorrect parameters. Please use -h option to show usage"),

    MAIN_VOLUME_REQUIRED_WHEN_DOWNLOADING_CHAPTER(11, "When specifying a chapter, you must also " +
            "specify a volume as some mangas have multiple chapters with the same number, e.g. 'Detective Conan'."),

    JSON_MANAGER_ERROR_WHILE_READING_JSON_FILE(20, "Error while reading the JSON file."),

    JSON_MANAGER_PROVIDED_SOURCE_DIFFERENT_THAN_PREVIOUSLY_PROVIDED(21, "The provided source is different than the one previously provided. "
            + "Please continue downloading with %s or start from scratch with another source."),

    JSON_MANAGER_UNABLE_TO_WRITE_TO_JSON_FILE(22, "Unable to write to the JSON file.\n\n%s")
}
