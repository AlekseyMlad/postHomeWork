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
        service.clear()
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
        val comment1 = Comment(fromId = 789, date = 1678886400, text = "Первый комментарий", noteId = 68)

        val addedComment1 = WallService.createComment(addedPost1.id, comment1)

        assertEquals(comment1.fromId, addedComment1.fromId)
        assertEquals(comment1.date, addedComment1.date)
        assertEquals(comment1.text, addedComment1.text)
    }

    @Test(expected = PostNotFoundException::class)
    fun shouldThrow() {
        val comment1 = Comment(fromId = 789, date = 1678886400, text = "Первый комментарий", noteId = 15)

        WallService.createComment(999, comment1)
    }
    

    val service = NoteService()


    @Test
    fun addNote() {
        val newNote = Note(title = "title", text = "text")
        val note = service.add(newNote)

        assertEquals(1, note.id)

    }

    @Test
    fun deleteNote() {

        val newNote = Note(title = "title", text = "text")
        val note = service.add(newNote)
        val deleteResult = service.delete(note.id)

        assertEquals(true, deleteResult)

    }

    @Test(expected = NoteNotFoundException::class)
    fun deleteNoteNoteNotFound() {

        service.delete(10)

    }

    @Test
    fun editNote() {
        val newNote = Note(title = "title", text = "text")
        val note = service.add(newNote)
        val editResult = service.edit(note.id, "newTitle", "newText")
        val editedNote = service.getById(note.id)

        assertEquals(true, editResult)
        assertEquals("newTitle", editedNote.title)
        assertEquals("newText", editedNote.text)
    }

    @Test(expected = NoteNotFoundException::class)
    fun editNoteNoteNotFound() {
        service.edit(10, "newTitle", "newText")

    }

    @Test
    fun getNotes() {
        val newNote1 = Note(title = "title1", text = "text1")
        val newNote2 = Note(title = "title2", text = "text2")
        service.add(newNote1)
        service.add(newNote2)
        val notes = service.get()

        assertEquals(2, notes.size)
    }

    @Test
    fun getNoteById() {
        val service = NoteService()
        service.clear()
        val newNote1 = Note(title = "title1", text = "text1", id = 1)
        val newNote2 = Note(title = "title2", text = "text2", id = 2)
        service.add(newNote1)
        service.add(newNote2)
        val note = service.getById(newNote1.id)

        assertEquals(newNote1.title, note.title)
        assertEquals(newNote1.text, note.text)
    }

    @Test(expected = NoteNotFoundException::class)
    fun getNoteByIdNoteNotFound() {
        service.getById(10)
    }

    @Test
    fun createComment() {
        val newNote = Note(title = "title", text = "text")
        val note = service.add(newNote)
        val newComment = Comment(noteId = note.id, text = "commentText", fromId = 231)
        val comment = service.createComment(note.id, newComment)

        assertEquals(newComment.text, comment.text)
        assertEquals(note.id, comment.noteId)

    }

    @Test(expected = NoteNotFoundException::class)
    fun createCommentNoteNotFound() {
        val newComment = Comment(noteId = 10, text = "commentText", fromId = 254)

        service.createComment(10, newComment)
    }

    @Test
    fun deleteComment() {
        val newNote = Note(title = "title", text = "text")
        val note = service.add(newNote)
        val newComment = Comment(noteId = note.id, text = "commentText", fromId = 137)
        val comment = service.createComment(note.id, newComment)
        val deleteResult = service.deleteComment(comment.id)

        assertTrue(deleteResult)
    }

    @Test(expected = CommentNotFoundException::class)
    fun deleteCommentCommentNotFound() {
        service.deleteComment(10)
    }

    @Test
    fun editComment() {
        val service = NoteService()
        service.clear()
        val newNote = Note(title = "title", text = "text", id = 1)
        val note = service.add(newNote)
        val newComment = Comment(noteId = note.id, text = "commentText", fromId = 542, id = 1)
        val comment = service.createComment(note.id, newComment)
        val editResult = service.editComment(comment.id, "newCommentText")
        val editedComment = service.getComment(note.id)[0]

        assertTrue(editResult)
        assertEquals("newCommentText", editedComment.text)

    }

    @Test(expected = CommentNotFoundException::class)
    fun editCommentCommentNotFound() {
        val service = NoteService()
        service.clear()
        service.editComment(10, "newCommentText")

    }

    @Test
    fun getComments() {
        val service = NoteService()
        service.clear()
        val newNote = Note(title = "title", text = "text")
        val note = service.add(newNote)
        val newComment1 = Comment(noteId = note.id, text = "commentText1", fromId = 15)
        val newComment2 = Comment(noteId = note.id, text = "commentText2", fromId = 12)
        service.createComment(note.id, newComment1)
        service.createComment(note.id, newComment2)
        val comments = service.getComment(note.id)

        assertEquals(2, comments.size)
    }

    @Test
    fun getCommentsNoteNotFound() {
        try {
            service.getComment(10)
        } catch (e: NoteNotFoundException) {

            assertEquals("Заметка с id 10 не найдена или уже удалена", e.message)
        }
    }
    @Test
    fun restoreComment() {
        val newNote = Note(title = "title", text = "text")
        val note = service.add(newNote)
        val newComment = Comment(noteId = note.id, text = "commentText", fromId = 31)
        val comment = service.createComment(note.id, newComment)
        service.deleteComment(comment.id)
        val restoreResult = service.restoreComment(comment.id)
        val restoredComment = service.getComment(note.id)[0]

        assertEquals(true, restoreResult)
        assertEquals(comment.text, restoredComment.text)

    }

    @Test(expected = CommentNotFoundException::class)
    fun restoreCommentCommentNotFound() {
        service.restoreComment(10)

    }

    @Test(expected = CommentNotFoundException::class)
    fun restoreCommentCommentNotDeleted() {
        val service = NoteService()
        service.clear()
        val newNote = Note(title = "title", text = "text")
        val note = service.add(newNote)
        val newComment = Comment(noteId = note.id, text = "commentText", fromId = 324)
        val comment = service.createComment(note.id, newComment)

        service.restoreComment(comment.id)


    }
}