package ru.netology

import ru.netology.interfaces.Attachment
import ru.netology.interfaces.*


data class Post(
    val id: Int = 0,
    val ownerId: Int = 0,
    val fromId: Int = 0,
    val createdBy: Int? = null,
    val date: Int = 0,
    val text: String = "text",
    val replyOwnerId: Int? = null,
    val replyPostId: Int = 0,
    val friendsOnly: Boolean = false,
    val canPin: Boolean = true,
    val canDelete: Boolean = true,
    val canEdit: Boolean = true,
    val isPinned: Boolean = false,
    val markedAsAds: Boolean = false,
    val isFavorite: Boolean = false,
    val likes: Likes = Likes(),
    val comments: Comments = Comments(),
    val attachments: List<Attachment> = emptyList()
)

data class Likes(
    val count: Int = 0,
    val userLikes: Boolean = false,
    val canLike: Boolean = true,
    val canPublish: Boolean = true
)

data class Comments(
    val count: Int = 0,
    val canPost: Boolean = true,
    val groupsCanPost: Boolean = true
)


object WallService {
    private var posts = emptyArray<Post>()
    private var nextId = 1

    fun clear() {
        posts = emptyArray()
        nextId = 1
    }

    fun add(post: Post): Post {
        val newPost = post.copy(id = nextId)
        posts += newPost
        nextId++
        return newPost
    }

    fun update(post: Post): Boolean {
        for ((index, currentPost) in posts.withIndex()) {
            if (currentPost.id == post.id) {
                posts[index] = post.copy()
                return true
            }
        }
        return false
    }

}

fun main() {
    // Создание постов
    val post1 = Post(
        ownerId = 123,
        fromId = 456,
        text = "Первый пост",
        likes = Likes(count = 10, userLikes = true),
        comments = Comments(count = 5),
    )
    val post2 = Post(ownerId = 789, text = "Второй пост", canPin = false)

    val photo = Photo(
        id = 1,
        userId = 300,
        text = "Photo",
        date = 162623
    )

    val video = Video(
        id = 1,
        userId = 300,
        text = "Video",
        date = 1852788,
    )

    val audio = Audio(
        id = 100,
        userId = 200,
        text = "Audio",
        date = 25041953,
        url = "audio.mp3"
    )

    val post3 = Post(
        ownerId = 123,
        fromId = 456,
        text = "Первый пост с фото",
        attachments = listOf(PhotoAttachment(photo)),
    )


    val post4 = Post(
        ownerId = 789,
        text = "Второй пост с видео и аудио",
        attachments = listOf(VideoAttachment(video), AudioAttachment(audio))
    )


    // Добавление постов
    val addedPost1 = WallService.add(post1)
    val addedPost2 = WallService.add(post2)
    val addedPost3 = WallService.add(post3)
    val addedPost4 = WallService.add(post4)

    println("Добавлен пост 1: $addedPost1")
    println("Добавлен пост 2: $addedPost2")

    // Обновление поста
    val updatedPost1 = addedPost1.copy(text = "Обновленный первый пост", likes = Likes(count = 15))
    val updateResult = WallService.update(updatedPost1)
    println("Обновление поста 1: $updateResult")


}
