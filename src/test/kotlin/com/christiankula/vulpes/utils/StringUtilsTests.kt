package com.christiankula.vulpes.utils

import org.junit.Assert
import org.junit.Test

class StringUtilsTests {

    @Test
    fun ShouldDoNothingWithEmptyString() {
        Assert.assertEquals("", StringUtils.stripAccents(""))
    }

    @Test
    fun ShouldStripAccentsFromGraveAccentedVowels() {
        Assert.assertEquals("a, e, i, o, u, A, E, I, O, U", StringUtils.stripAccents("à, è, ì, ò, ù, À, È, Ì, Ò, Ù"))
    }

    @Test
    fun ShouldStripAccentsFromAcuteAccentedVowels() {
        Assert.assertEquals("a, e, i, o, u, y, A, E, I, O, U, Y", StringUtils.stripAccents("á, é, í, ó, ú, ý, Á, É, Í, Ó, Ú, Ý"))
    }

    @Test
    fun ShouldStripAccentsFromVowelsWithCaret() {
        Assert.assertEquals("a, e, i, o, u, A, E, I, O, U", StringUtils.stripAccents("â, ê, î, ô, û, Â, Ê, Î, Ô, Û"))
    }

    @Test
    fun ShouldStripAccentsFromVowelsWithColon() {
        Assert.assertEquals("a, e, i, o, u, y, A, E, I, O, U, Y", StringUtils.stripAccents("ä, ë, ï, ö, ü, ÿ, Ä, Ë, Ï, Ö, Ü, Ÿ"))
    }

    @Test
    fun ShouldStripAccentsFromLettersWithTilde() {
        Assert.assertEquals("a, n, o, A, N, O", StringUtils.stripAccents("ã, ñ, õ, Ã, Ñ, Õ"))
    }

    @Test
    fun ShouldStripAccentsFromAWithDot() {
        Assert.assertEquals("a, A", StringUtils.stripAccents("å, Å"))
    }
}