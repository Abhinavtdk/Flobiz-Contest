package com.example.flobizcontest.utils

import android.content.Context
import android.net.Uri
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import com.example.flobizcontest.model.Item
import java.text.SimpleDateFormat
import java.util.*

fun TextView.getTime(time: Long) {
    val date = Date(time)
    val timeFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
    this.text = timeFormat
}

fun Fragment.openQuestion(questtionItem: Item, context: Context) {
    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()

    customTabsIntent.launchUrl(context, Uri.parse(questtionItem.link))
}