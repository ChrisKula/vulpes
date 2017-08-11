package com.christiankula.vulpes.net.connection

import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

private val USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36"

private val HEADER_ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
private val HEADER_ACCEPT_CHARSET = "ISO-8859-1,utf-8;q=0.7,*;q=0.3"
private val HEADER_LANGUAGE = "en-US,en;q=0.8"
private val HEADER_KEEP_ALIVE = "keep-alive"

private val TIME_OUT_IN_MILLIS = 5000

class ConnectionFactory {

    companion object {
        fun createJsoupConnection(baseUrl: String, parser: Parser): Connection {
            return Jsoup.connect(baseUrl)
                    .userAgent(USER_AGENT)
                    .header("Accept", HEADER_ACCEPT)
                    .header("Accept-Charset", HEADER_ACCEPT_CHARSET)
                    .header("Accept-language", HEADER_LANGUAGE)
                    .header("keep-alive", HEADER_KEEP_ALIVE)
                    .timeout(TIME_OUT_IN_MILLIS)
                    .parser(parser)
        }
    }
}