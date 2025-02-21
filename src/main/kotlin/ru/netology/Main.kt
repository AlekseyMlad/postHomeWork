package ru.netology

import ru.netology.interfaces.Attachment
import ru.netology.interfaces.*

interface Entity {
    val id: Int
}

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
    val id: Int = 0,
    val count: Int = 0,
    val canPost: Boolean = true,
    val groupsCanPost: Boolean = true
)
data class Comment(
    override val id: Int = 0,
    val noteId: Int,
    val fromId: Int,
    val date: Int = 0,
    val text: String,
    val isDeleted: Boolean = false
) : Entity

data class Note(
    override val id: Int = 0,
    val title: String,
    val text: String,
    val date: Int = 0,
    val privacy: String = "all",
    val commentPrivacy: String = "all",
    val isDeleted: Boolean = false
) : Entity


//Исключение
class PostNotFoundException(message: String) : RuntimeException(message)
class NoteNotFoundException(message: String) : RuntimeException(message)
class CommentNotFoundException(message: String) : RuntimeException(message)


class NoteService{
    private var notes = mutableListOf<Note>()
    private var comments = mutableListOf<Comment>()
    private var nextNoteId = 1
    private var nextCommentId = 1

    fun add(note: Note): Note {//Создает новую заметку у текущего пользователя.
        val newNote = note.copy(id = nextNoteId)
        notes.add(newNote)
        nextNoteId ++
        return newNote
    }

    fun delete(noteId: Int): Boolean {//Удаляет заметку текущего пользователя.
        var noteFound = false
        for (i in 0 until notes.size) {
            if (notes[i].id == noteId && !notes[i].isDeleted) {
                notes[i] = notes[i].copy(isDeleted = true)
                noteFound = true
                break
            }
        }
        if (!noteFound) {
            throw NoteNotFoundException("Заметка с id $noteId не найдена или уже удалена")
        }
        return true
    }

    fun edit(noteId: Int, newTitle: String, newText: String): Boolean {//Редактирует заметку текущего пользователя.
        var noteFound = false
        for (i in 0 until notes.size) {
            if (notes[i].id == noteId && !notes[i].isDeleted) {
                notes[i] = notes[i].copy(title = newTitle, text = newText)
                noteFound = true
                break
            }
        }
        if (!noteFound) {
            throw NoteNotFoundException("Заметка с id $noteId не найдена или уже удалена")
        }
        return true
    }

    fun get(): List<Note> {//Возвращает список заметок, созданных пользователем.
        val result = mutableListOf<Note>()
        for (note in notes) {
            if (!note.isDeleted) {
                result.add(note)
            }
        }
        return result
    }

    fun getById(noteId: Int): Note {//Возвращает заметку по её id.
        var foundNote: Note? = null
        for (note in notes) {
            if (note.id == noteId && !note.isDeleted) {
                foundNote = note
                break
            }
        }
        return foundNote ?: throw NoteNotFoundException("Заметка с id $noteId не найдена или уже удалена")
    }

    fun createComment(noteId: Int, comment: Comment): Comment {//Добавляет новый комментарий к заметке.
        var noteFound = false
        for (note in notes) {
            if (note.id == noteId && !note.isDeleted) {
                noteFound = true
                break
            }
        }
        if (!noteFound) {
            throw NoteNotFoundException("Заметка с id $noteId не найдена или уже удалена")
        }

        val newComment = comment.copy(id = nextCommentId, noteId = noteId)
        comments.add(newComment)
        nextCommentId++
        return newComment
    }

    fun deleteComment(commentId: Int): Boolean {//Удаляет комментарий к заметке.
        var commentFound = false
        for (i in 0 until comments.size) {
            if (comments[i].id == commentId && !comments[i].isDeleted) {
                comments[i] = comments[i].copy(isDeleted = true)
                commentFound = true
                break
            }
        }
        if (!commentFound) {
            throw CommentNotFoundException("Комментарий с id $commentId не найден или уже удалён")
        }
        return true
    }

    fun editComment(commentId: Int, newText: String): Boolean {//Редактирует указанный комментарий у заметки.
        var commentFound = false
        for (i in 0 until comments.size) {
            if (comments[i].id == commentId && !comments[i].isDeleted) {
                comments[i] = comments[i].copy(text = newText)
                commentFound = true
                break
            }
        }
        if (!commentFound) {
            throw CommentNotFoundException("Комментарий с id $commentId не найден или уже удалён")
        }
        return true
    }

    fun getComment(noteId: Int): List<Comment> {//Возвращает список комментариев к заметке.
        var noteFound = false
        for (note in notes) {
            if (note.id == noteId && !note.isDeleted) {
                noteFound = true
                break
            }
        }
        if (!noteFound) {
            throw NoteNotFoundException("Заметка с id $noteId не найдена или уже удалена")
        }
        val result = mutableListOf<Comment>()
        for (comment in comments) {
            if (comment.noteId == noteId && !comment.isDeleted) {
                result.add(comment)
            }
        }
        return result
    }

    fun restoreComment(commentId: Int): Boolean {//Восстанавливает удалённый комментарий.
        var commentFound = false
        for (i in 0 until comments.size) {
            if (comments[i].id == commentId && comments[i].isDeleted) {
                comments[i] = comments[i].copy(isDeleted = false)
                commentFound = true
                break
            }
        }
        if (!commentFound) {
            throw CommentNotFoundException("Удалённый комментарий с id $commentId не найден")
        }
        return true

    }

    fun clear() {
        notes.clear()
        comments.clear()
        nextNoteId = 1
        nextCommentId = 1
    }
}


object WallService {
    private var posts = emptyArray<Post>()
    private var comments = emptyArray<Comment>()
    private var nextId = 1
    private var nextCommentId = 1


    fun clear() {
        posts = emptyArray()
        comments = emptyArray()
        nextId = 1
        nextCommentId = 1
    }


    fun createComment(postId: Int, comment: Comment): Comment {
        var postExists = false
        for (post in posts) {
            if (post.id == postId) {
                postExists = true
                break
            }
        }

        if (!postExists) {
            throw PostNotFoundException("Post with id $postId not found")
        }

        val newComment = comment.copy(id = nextCommentId)
        comments += newComment
        nextCommentId++
        return newComment
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

    // Создание комментария к посту
    try {
        val comment1 = Comment(noteId = 126, fromId = 789, date = 1678886400, text = "Первый комментарий")
        val addedComment1 = WallService.createComment(addedPost1.id, comment1)
        println("Added comment: $addedComment1")
    } catch (e: PostNotFoundException) {
        println("Error: ${e.message}")
    }

    // Попытка создать комментарий к несуществующему посту
    try {
        val comment2 = Comment(noteId = 1223, fromId = 1011, date = 1678887400, text = "Комментарий к несуществующему посту")
        val addedComment2 = WallService.createComment(999, comment2)
        println("Added comment: $addedComment2")
    } catch (e: PostNotFoundException) {
        println("Error: ${e.message}")
    }

    val service = NoteService()

    val note1 = Note(
        title = "Первая заметка",
        text = "Текст первой заметки"
    )

    val note2 = Note(
        title = "Вторая заметка",
        text = "Текст второй заметки"
    )

    val addedNote1 = service.add(note1)
    val addedNote2 = service.add(note2)

    println("Добавлена заметка 1: $addedNote1")
    println("Добавлена заметка 2: $addedNote2")

    val allNotes = service.get()
    println("Список всех заметок: $allNotes")

    try {
        service.edit(addedNote1.id, "Обновленная заметка", "Новый текст")
        println("Заметка ${addedNote1.id} успешно отредактирована")
    } catch (e: NoteNotFoundException) {
        println("Ошибка при редактировании заметки: ${e.message}")
    }

    val comment1 = Comment(noteId = addedNote1.id, text = "Отличная заметка!", fromId = 524)

    val addedComment1 = service.createComment(addedNote1.id, comment1)
    println("Добавлен комментарий 1: ${addedComment1.text}")

    try {
        service.deleteComment(addedComment1.id)
        println("Комментарий ${addedComment1.id} успешно удален")
    } catch (e: CommentNotFoundException) {
        println("Ошибка при удалении комментария: ${e.message}")
    }
    try{
        service.restoreComment(addedComment1.id)
        println("Комментарий ${addedComment1.id} успешно восстановлен")
    } catch (e: CommentNotFoundException){
        println("Ошибка при восстановлении  комментария: ${e.message}")
    }

}
