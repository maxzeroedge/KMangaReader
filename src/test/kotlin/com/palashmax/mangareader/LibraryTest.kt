/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.palashmax.mangareader

import java.util.*
import java.util.stream.IntStream
import kotlin.test.Test
import kotlin.test.assertTrue

class LibraryTest {
    @Test fun testSomeLibraryMethod() {
        val classUnderTest = MangaReader()
        // assertTrue(classUnderTest.someLibraryMethod(), "someLibraryMethod should return 'true'")
    }
}

fun main() {
    val mr = MangaReader()
    val titles = mr.fetchTitles()
    IntStream.range(0, titles.size)
            .forEach { v->println("$v: ${titles[v].get("name")}") }
    println("Please make a selection: ")
    val selection = Scanner(System.`in`).nextInt()
    val chapters = mr.fetchChapters(titles[selection].get("url") as String)
    chapters.stream().forEach { chapter-> run {
        val pages = mr.fetchPageImages(chapter.get("url") as String)
        pages.parallelStream().forEach { imageUrl -> run {
            mr.downloadImage(imageUrl)
        } }
    } }
}