package com.grandl.rankwidget

import android.content.Context
import org.jsoup.Jsoup
import java.time.LocalDate

data class RankData(val tier:String,val division:String,val lp:Int,val wins:Int,val losses:Int,val wr:Int)

object RankRepository {
    const val PROFILE_URL = "https://op.gg/tr/lol/summoners/tr/grandl-wave"
    private val rankRe = Regex("(Iron|Bronze|Silver|Gold|Platinum|Emerald|Diamond|Master|Grandmaster|Challenger)\\s*([1-4IVX]*)\\s+(\\d+)\\s+LP", RegexOption.IGNORE_CASE)
    private val wlRe = Regex("(\\d+)G\\s+(\\d+)M\\s+Kazanma oranı\\s+(\\d+)%", RegexOption.IGNORE_CASE)

    fun fetch(): RankData {
        val doc = Jsoup.connect(PROFILE_URL).userAgent("Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 Chrome/120 Mobile Safari/537.36").timeout(20000).get()
        val text = doc.body().wholeText().replace(Regex("\\s+"), " ")
        val solo = text.substringAfter("Dereceli Tek/Çift").substringBefore("Dereceli Esnek")
        val r = rankRe.find(solo) ?: rankRe.find(text) ?: error("Solo/Duo rank bulunamadı")
        val w = wlRe.find(solo) ?: wlRe.find(text) ?: error("Solo/Duo W/L bulunamadı")
        return RankData(r.groupValues[1].replaceFirstChar { it.uppercase() }, r.groupValues[2], r.groupValues[3].toInt(), w.groupValues[1].toInt(), w.groupValues[2].toInt(), w.groupValues[3].toInt())
    }

    fun saveAndDailyDelta(context: Context, data: RankData): Int {
        val p = context.getSharedPreferences("rank", Context.MODE_PRIVATE)
        val today = LocalDate.now().toString()
        val oldDay = p.getString("day", null)
        if (oldDay != today) p.edit().putString("day", today).putInt("baseLp", data.lp).putString("baseTier", data.tier).putString("baseDiv", data.division).apply()
        // Exact cross-tier LP arithmetic cannot be inferred reliably from a snapshot; within the same division it is exact.
        val same = p.getString("baseTier", data.tier)==data.tier && p.getString("baseDiv", data.division)==data.division
        val delta = if (same) data.lp - p.getInt("baseLp", data.lp) else 0
        p.edit().putString("tier",data.tier).putString("div",data.division).putInt("lp",data.lp).putInt("wins",data.wins).putInt("losses",data.losses).putInt("wr",data.wr).putInt("delta",delta).putLong("updated",System.currentTimeMillis()).apply()
        return delta
    }
}
