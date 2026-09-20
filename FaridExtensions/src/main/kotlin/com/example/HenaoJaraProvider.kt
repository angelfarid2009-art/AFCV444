package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.nodes.Element

class HenaoJaraProvider : MainAPI() {
    override var mainUrl = "https://henaojara.com"
    override var name = "HenaoJara"
    override val hasMainPage = true
    override var lang = "es"
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie, TvType.OVA)

    override val mainPage = mainPageOf(
        "$mainUrl/" to "Recientes",
        "$mainUrl/todos-los-animes/" to "Todos"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = if (page == 1) request.data else "${request.data}page/$page/"
        val document = app.get(url).document
        val home = document.select("article").mapNotNull { it.toSearch() }
        return newHomePageResponse(request.name, home)
    }

    private fun Element.toSearch(): SearchResponse? {
        val a = selectFirst("a") ?: return null
        val href = a.attr("href")
        if (href.isBlank()) return null
        val title = selectFirst("h2, h3")?.text() ?: a.attr("title")
        if (title.isBlank()) return null
        val img = selectFirst("img")?.attr("data-src") ?: selectFirst("img")?.attr("src") ?: ""
        return newAnimeSearchResponse(title, href, TvType.Anime) {
            this.posterUrl = fixUrlNull(img)
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val doc = app.get("$mainUrl/?s=$query").document
        return doc.select("article").mapNotNull { it.toSearch() }
    }

    override suspend fun load(url: String): LoadResponse {
        val doc = app.get(url).document
        val title = doc.selectFirst("h1")?.text() ?: "Anime"
        val poster = doc.selectFirst(".poster img, img.wp-post-image")?.attr("src")
        val plot = doc.selectFirst(".Description p, .sinopsis")?.text()
        val episodes = doc.select("ul.episodes li a, .episodios li a").mapNotNull {
            val epUrl = it.attr("href")
            if (epUrl.isBlank()) null else newEpisode(epUrl) { this.name = it.text() }
        }.reversed()
        return if (episodes.isNotEmpty()) {
            newTvSeriesLoadResponse(title, url, TvType.Anime, episodes) {
                this.posterUrl = poster
                this.plot = plot
            }
        } else {
            newMovieLoadResponse(title, url, TvType.AnimeMovie, url) {
                this.posterUrl = poster
                this.plot = plot
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val doc = app.get(data).document
        doc.select("iframe").forEach {
            val src = it.attr("src")
            if (src.isNotBlank()) loadExtractor(src, data, subtitleCallback, callback)
        }
        return true
    }
}
