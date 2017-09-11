package com.christiankula.vulpes.utils

import java.text.Normalizer

class StringUtils {
    companion object {
        /**
         * Keeps only alphabetical characters, numbers, spaces and underscores
         */
        fun clean(str: String): String {
            return str.replace("_", " ").replace("[^0-9a-zA-Z ]".toRegex(), "")
        }

        /**
         * Strips every accent in the String, replacing accented letters by their non-accented counterpart if possible
         */
        fun stripAccents(str: String): String {
            var s = str
            s = Normalizer.normalize(s, Normalizer.Form.NFD)
            s = s.replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
            return s
        }
    }
}