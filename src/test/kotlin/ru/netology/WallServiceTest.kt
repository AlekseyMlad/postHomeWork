package ru.netology

import org.junit.Test

import org.junit.Assert.*

class WallServiceTest {
   

    val post1 = Post(
        ownerId = 123,
        fromId = 456,
        text = "Первый пост",
        likes = Likes(count = 10, userLikes = true),
        comments = Comments(count = 5)
    )
    val addedPost1 = WallService.add(post1)
    val post2 = Post(ownerId = 789, text = "Второй пост", canPin = false)

    @Test
    fun testIdAdd() {

        val result = WallService.add(post1)
        assertEquals(true, result.id > 0)
    }

    @Test
    fun testUpdate() {

        val result = WallService.update(post2)
        assertEquals(false, result)
    }

    val updatedPost1 = addedPost1.copy(text = "Обновленный первый пост", likes = Likes(count = 15))

    @Test
    fun test2Update() {

        val result = WallService.update(updatedPost1)
        assertEquals(true, result)
    }


}