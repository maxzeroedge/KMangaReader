/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.palashmax.mangareader

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result;
import org.jsoup.Jsoup

class MangaReader {
    fun fetchTitles(): Boolean {
        val (request, response, result) = "https://www.mangareader.net/alphabetical"
            .httpGet()
            .responseString()

        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                println(ex)
            }
            is Result.Success -> {
                val data = result.get()
                var doc = Jsoup.parse(data)
                // doc.getElementsByClass("series_alpha")
                var series_alpha_uls = doc.select("ul.series_alpha")
                series_alpha_uls.stream().map { series_alpha_ul -> {
                    var series_alpha_ul_lis = series_alpha_ul.select("li")
                    series_alpha_ul_lis.stream().map{ series_alpha_ul_li -> {
                        series_alpha_ul_li.text()
                    } }
                } }.toArray()
                print(series_alpha_uls)
            }
        }
        return true
    }
}

fun main() {
    val mr = MangaReader()
    mr.fetchTitles()
}