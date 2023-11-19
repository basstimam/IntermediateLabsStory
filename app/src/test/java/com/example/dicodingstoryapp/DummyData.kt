package com.example.dicodingstoryapp

import com.example.dicodingstoryapp.data.remote.ListStoryItem

object DummyData {
    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                createdAt = null,
                name = "name + $i",
                description = "desc + $i",
                lat = 0.1,
                lon = 0.1,



            )
            items.add(story)
        }
        return items
    }
}