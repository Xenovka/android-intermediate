package com.dicoding.storyapp

import com.dicoding.storyapp.api.StoryItem

object  DummyData {
    fun generateDummyData(): List<StoryItem> {
        val stories: MutableList<StoryItem> = arrayListOf()

        for (i in 0..10) {
            val story = StoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1684290616610_k6lRNda-.jpg",
                "2023-05-17T02:30:16.624Z",
                "Wendy",
                "PagingData",
                (-6.303576).toFloat(),
                "story-o4eiq9wupBxWyrKk",
                (106.887832).toFloat()
            )
            stories.add(story)
        }

        return stories
    }

}