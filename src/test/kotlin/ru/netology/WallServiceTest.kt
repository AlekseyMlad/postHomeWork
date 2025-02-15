package ru.netology

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class WallServiceTest {


    val post1 = Post(
        id = 1,
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
    val updatedPost2 = addedPost2.copy(id = 15, text = "Обновленный второй пост", likes = Likes(count = 15))

    @Test
    fun testTrueUpdate() {
        WallService.add(post1)
        val result = WallService.update(updatedPost1.copy(text = "Обновленный первый пост"))
        assertTrue(result)
    }

    @Test
    fun testFalseUpdate() {
        WallService.add(post2)
        val result = WallService.update(updatedPost2)
        assertEquals(false, result)
    }

    @Test
    fun notShouldThrow() {
        val post1 = Post(ownerId = 123, fromId = 456, text = "Первый пост")
        val addedPost1 = WallService.add(post1)
        val comment1 = Comment(fromId = 789, date = 1678886400, text = "Первый комментарий")

        val addedComment1 = WallService.createComment(addedPost1.id, comment1)

        assertEquals(comment1.fromId, addedComment1.fromId)
        assertEquals(comment1.date, addedComment1.date)
        assertEquals(comment1.text, addedComment1.text)
    }

    @Test(expected = PostNotFoundException::class)
    fun shouldThrow() {
        val comment1 = Comment(fromId = 789, date = 1678886400, text = "Первый комментарий")

        WallService.createComment(999, comment1)
    }

}
