package com.anas.aiassistant.data

import com.anas.aiassistant.model.ChatForDB

object AppData {

    var chats = arrayListOf<ChatForDB>()
    val suggestions = listOf("Write a short story about a cat who discovers a secret portal to another dimension",
        "What measures can be taken to promote equality in the workplace?",
        "Share a recipe for a delicious dish",
        "Discuss the latest advancements in artificial intelligence",
        "Suggest ways to improve software development skills",
        "Discuss the impact of renewable energy",
        "Come up with a list of funny 'would you rather' questions.",
        "What strategies foster a healthy work-life balance in different cultures?",
        "How can education systems adapt to better prepare students for the future?"
    )
}