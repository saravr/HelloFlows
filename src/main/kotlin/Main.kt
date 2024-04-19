package org.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

data class Document(
    val id: Int,
    val title: String,
    val author: String,
    val pageCount: Int,
)

data class Saved(
    val documentId: Int,
    val saved: Boolean,
)

val documents = listOf(
    Document(1, "A story book", "Job B", 121),
    Document(2, "Impressions", "Clark F", 520),
    Document(3, "Wild cats", "J. Brown", 347),
    Document(4, "Bob's memoirs", "Kalki K", 98),
    Document(5, "Travel guide", "Sandilyan B", 155),
    Document(6, "A billionaire's musings", "Tom B", 458),
    Document(7, "Watch tower", "Jack Ryan", 819),
    Document(8, "Deception Point", "Dan Brown", 1029),
    Document(9, "Jurassic Park", "Michael Crichton", 672),
    Document(10, "European travels", "Rick Steves", 309),
)

val saved = listOf(
    Saved(1, false),
    Saved(2, false),
    Saved(3, false),
    Saved(4, false),
    Saved(5, false),
    Saved(6, false),
    Saved(7, false),
    Saved(8, false),
    Saved(9, false),
    Saved(10, false),
)

fun main() {
    val documentsFlow = flow {
        emit(documents)
    }

    val savedFlow = flow {
        emit(saved)
        delay(2000)
        val saved1 = saved.toMutableList()
        saved1[0] = saved[0].copy(saved = true)
        emit(saved1)
        delay(3000)
        val saved2 = saved1.toMutableList()
        saved2[4] = saved1[4].copy(saved = true)
        emit(saved2)
        delay(5000)
        val saved3 = saved2.toMutableList()
        saved3[7] = saved2[7].copy(saved = true)
        emit(saved3)
    }

    runBlocking {
        val combinedFlow = combine(documentsFlow, savedFlow) { d, s ->
            Pair(d, s)
        }

        combinedFlow.collectLatest { (docList, savedList) ->
            println("++++++++++++++ DOC LIST")
            docList.forEach { doc ->
                val saved = savedList.find { it.documentId == doc.id }?.saved ?: false
                val savedStr = if (saved) ">>>>>> SAVED <<<<<<" else ""
                println("Got document: ${doc.title} -- $savedStr")
            }
        }
    }
}