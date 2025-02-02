package ru.netology

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class WallServiceTest {

   

    val post1 = Post(
        ownerId = 123,
        fromId = 456,
        text = "Первый пост",
        likes = Likes(count = 10, userLikes = true),
        comments = Comments(count = 5)
    )

    val post2 = Post(ownerId = 789, text = "Второй пост", canPin = false)
    val addedPost1 = WallService.add(post1)
    val addedPost2 = WallService.add(post2)
    
    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun testIdAdd() {

        val result = WallService.add(post1)
        assertTrue(result.id > 0)
    }




    @Test
    fun testUpdate() {
        val result = WallService.update(post2)
        assertFalse(result)
    }

    val updatedPost1 = addedPost1.copy(text = "Обновленный первый пост", likes = Likes(count = 15))
    val updatedPost2 = addedPost2.copy(id = 15, text = "Обновленный первый пост", likes = Likes(count = 15))

    @Test
    fun test2Update() {
        WallService.add(post1)
        val result = WallService.update(updatedPost1)
        assertEquals(true, result)
    }

    @Test
    fun test3Update() {
        WallService.add(post2)
        val result = WallService.update(updatedPost2)
        assertEquals(false, result)
    }

}
