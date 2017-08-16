package com.christiankula.vulpes.file.filters

import com.christiankula.vulpes.file.ImageFileExtension
import java.io.File
import java.io.FileFilter

class ImageFileFilter : FileFilter {
    override fun accept(pathname: File): Boolean {
        if (pathname.isHidden) {
            return false
        }

        return ImageFileExtension.values().any { pathname.name.endsWith(it.extension) }
    }
}