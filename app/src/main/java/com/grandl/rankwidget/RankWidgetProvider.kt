package com.grandl.rankwidget
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
class RankWidgetProvider:AppWidgetProvider(){override fun onUpdate(c:Context,m:AppWidgetManager,ids:IntArray){ids.forEach{update(c,m,it)}}
 companion object{fun badge(t:String)=when(t.lowercase()){"iron"->"⚙";"bronze"->"◈";"silver"->"◇";"gold"->"◆";"platinum"->"✦";"emerald"->"❖";"diamond"->"💎";"master"->"♛";"grandmaster"->"♚";"challenger"->"👑";else->"✦"}
 fun updateAll(c:Context){val m=AppWidgetManager.getInstance(c);val cn=ComponentName(c,RankWidgetProvider::class.java);m.getAppWidgetIds(cn).forEach{update(c,m,it)}}
 private fun update(c:Context,m:AppWidgetManager,id:Int){val p=c.getSharedPreferences("rank",Context.MODE_PRIVATE);val tier=p.getString("tier","Platinum")!!;val d=p.getInt("delta",0);val v=RemoteViews(c.packageName,R.layout.rank_widget);v.setTextViewText(R.id.wBadge,badge(tier));v.setTextViewText(R.id.wRank,"$tier ${p.getString("div","1")}");v.setTextViewText(R.id.wLp,"${p.getInt("lp",30)} LP");v.setTextViewText(R.id.wRecord,"${p.getInt("wins",68)}W • ${p.getInt("losses",55)}L • %${p.getInt("wr",55)}");v.setTextViewText(R.id.wDelta,"BUGÜN ${if(d>=0) "+" else ""}$d LP");m.updateAppWidget(id,v)}}}
