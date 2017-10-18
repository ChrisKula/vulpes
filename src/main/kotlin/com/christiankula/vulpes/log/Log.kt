package com.christiankula.vulpes.log

class Log private constructor() {
    companion object {
        private const val ANSI_RESET = "\u001B[0m"
        private const val ANSI_BLACK = "\u001B[30m"
        private const val ANSI_RED = "\u001B[31m"
        private const val ANSI_GREEN = "\u001B[32m"
        private const val ANSI_YELLOW = "\u001B[33m"
        private const val ANSI_BLUE = "\u001B[34m"
        private const val ANSI_PURPLE = "\u001B[35m"
        private const val ANSI_CYAN = "\u001B[36m"
        private const val ANSI_WHITE = "\u001B[37m"

        /**
         * Outputs plainly the message in the standard output.
         * * Standard text color
         * * No tag
         * * Nothing fancy
         */
        fun plain(message: String) {
            System.out.println(message)
        }

        /**
         * Outputs an info message with :
         * * Standard text color
         * * `INFO` tag
         */
        fun info(message: String) {
            System.out.println("[INFO] $message")
        }

        /**
         * Outputs a warning message with :
         * * Yellow text color
         * * `WARN` tag
         */
        fun warning(message: String) {
            System.out.println("$ANSI_YELLOW[WARN] $message$ANSI_RESET")
        }

        /**
         * Outputs an error message with :
         * * Red text color
         * * `ERROR` tag
         */
        fun error(message: String) {
            System.out.println("$ANSI_RED[ERROR] $message$ANSI_RESET")
        }

        /**
         * Outputs a tag-less custom message with :
         * * Blue text color
         * * No tag
         */
        fun custom(message: String) {
            System.out.println("$ANSI_BLUE$message$ANSI_RESET")
        }

        /**
         * Outputs a tag-less custom message with :
         * * Blue text color
         * * customized tag, if not empty string
         * @param tag custom tag
         */
        fun custom(tag: String, message: String) {
            if (tag.isEmpty()) {
                custom(message)
            } else {
                custom("[$tag] $message")
            }
        }

        /**
         * Shortcut method for [plain]
         * @see plain
         */
        fun p(message: String) {
            plain(message)
        }

        /**
         * Shortcut method for [info]
         * @see info
         */
        fun i(message: String) {
            info(message)
        }

        /**
         * Shortcut method for [warning]
         * @see warning
         */
        fun w(message: String) {
            warning(message)
        }

        /**
         * Shortcut method for [error]
         * @see error
         */
        fun e(message: String) {
            error(message)
        }

        /**
         * Shortcut method for [custom] with tag
         * @see custom
         */
        fun c(tag: String, message: String) {
            custom(tag, message)
        }

        /**
         * Shortcut method for [custom]
         * @see custom
         */
        fun c(message: String) {
            custom(message)
        }

    }
}
