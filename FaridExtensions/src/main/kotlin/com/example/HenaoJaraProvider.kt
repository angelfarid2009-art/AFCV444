package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class HenaoJaraProvider : MainAPI() {
    override var mainUrl = "https://henaojara.com"
    override var name = "HenaoJara"
    override val hasMainPage = true
    override var lang = "es"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(TvType.Anime)

    override val mainPage = mainPageOf(
        "/" to "Últimos"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val doc = app.get(mainUrl).document
        val list = doc.select("a:has(img)").mapNotNull {
            val href = it.attr("href")
            val title = it.attr("title").ifBlank { it.text() }.trim()
            if (title.isBlank() || href.isBlank()) return@mapNotNull null
            if (!href.contains("/anime/") && !href.contains("/ver/")) return@mapNotNull null
            newAnimeSearchResponse(title, fixUrl(href), TvType.Anime)
        }.distinctBy { it.url }.take(24)
        return newHomePageResponse(listOf(HomePageList("HenaoJara", list)))
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val doc = app.get("$mainUrl/?s=$query").document
        return doc.select("a:has(img)").mapNotNull {
            val href = it.attr("href")
            val title = it.attr("title").ifBlank { it.text() }.trim()
            if (title.isBlank() || href.isBlank()) return@mapNotNull null
            newAnimeSearchResponse(title, fixUrl(href), TvType.Anime)
        }.distinctBy { it.url }
    }

    override suspend fun load(url: String): LoadResponse {
        val doc = app.get(url).document
        val title = doc.selectFirst("h1")?.text()?.trim() ?: "HenaoJara"
        val poster = doc.selectFirst("meta[property=og:image]")?.attr("content")
        val episodes = doc.select("a[href*=/episodio-], a[href*=/ver/]").mapNotNull {
            val epUrl = it.attr("href")
            if (epUrl.isBlank()) return@mapNotNull null
            newEpisode(fixUrl(epUrl)) { 
                name = it.text().trim() 
            }
        }.distinctBy { it.data }.reversed()
        
        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.posterUrl = poster
            addEpisodes(DubStatus.Subbed, episodes)
        }
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        val doc = app.get(data).document
        doc.select("iframe").forEach {
            val src = it.attr("src").ifBlank { it.attr("data-src") }
            if (src.isNotBlank()) {
                try { loadExtractor(src, data, subtitleCallback, callback) } catch (e: Exception) {}
            }
        }
        return true
    }
}