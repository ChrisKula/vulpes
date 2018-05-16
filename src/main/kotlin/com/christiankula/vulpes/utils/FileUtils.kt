package com.christiankula.vulpes.utils

import com.christiankula.vulpes.log.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Buffer
import okio.BufferedSink
import okio.BufferedSource
import okio.Okio
import java.io.File
import java.io.IOException

object FileUtils {
    fun copyUrlToFile(url: String, destinationFile: File) {
        if (!destinationFile.exists()) {
            destinationFile.parentFile.mkdirs()
            destinationFile.createNewFile()
        }

        val sink: BufferedSink = Okio.buffer(Okio.sink(destinationFile))
        var source: BufferedSource = Buffer()

        try {
            val request = Request.Builder().url(url).build()
            val response = OkHttpClient.Builder().build().newCall(request).execute()

            response.body()?.let {
                source = it.source()

                val sinkBuffer = sink.buffer()

                val bufferSize: Long = 8 * 1024

                var bytesRead: Long = 0

                while (bytesRead != (-1).toLong()) {
                    bytesRead = source.read(sinkBuffer, bufferSize)
                    sink.emit()
                }
            }

            response.close()
        } catch (ioe: IOException) {
            Log.e(ioe.localizedMessage)
        } finally {
            sink.flush()
            sink.close()

            source.close()
        }
    }
}
