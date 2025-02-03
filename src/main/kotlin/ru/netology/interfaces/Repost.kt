package ru.netology.interfaces

interface Attachment {
    val type: String
}
data class PhotoAttachment(val photo: Photo) : Attachment {
    override val type = "photo"
}

data class VideoAttachment(val video: Video) : Attachment {
    override val type = "video"
}

data class AudioAttachment(val audio: Audio) : Attachment {
    override val type = "audio"
}
data class DocAttachment(val doc: Doc) : Attachment {
    override val type = "doc"
}
data class LinkAttachment(val link: Link) : Attachment {
    override val type = "link"
}
// Классы для хранения данных
data class Photo(
    val id: Int,
    val userId: Int,
    val text: String,
    val date: Int,
)

data class Video(
    val id: Int,
    val userId: Int,
    val text: String,
    val date: Int,
)
data class Audio(
    val id: Int,
    val userId: Int,
    val text: String,
    val date: Int,
    val url: String
)
data class Doc(
    val id: Int,
    val userId: Int,
    val text: String,
    val date: Int,
    val url: String
)

data class Link(
    val url: String,
    val id: Int,
    val userId: Int,
    val text: String,
    val date: Int,
    val photo: Photo
)
