package com.christiankula.vulpes.cli.messages

enum class Error(val code: Int, val message: String) {
    MAIN_INCORRECT_PARAMETERS(10, "Incorrect parameters. Please use -h option to show usage"),

    MAIN_VOLUME_REQUIRED_WHEN_DOWNLOADING_CHAPTER(11, "When specifying a chapter, you must also " +
            "specify a volume as some mangas have multiple chapters with the same number, e.g. 'Detective Conan'.");
}
