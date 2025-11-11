package precourse.smartcloset.Board.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import precourse.smartcloset.user.entity.User
import java.time.LocalDateTime

@Entity
@Table(name = "boards")
class Board(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    val id: Long? = null,

    @Column(name = "title", nullable = false, length = 20)
    var title: String,

    @Column(name = "content", nullable = false, length = 100)
    var content: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "weather", nullable = false)
    var weather: WeatherType,

    @Column(name = "image_url")
    var imageUrl: String? = null,

    @Column(name = "tags")
    var tags: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun update(title: String, content: String, weather: WeatherType, imageUrl: String?, tags: String?) {
        this.title = title
        this.content = content
        this.weather = weather
        this.imageUrl = imageUrl
        this.tags = tags
        this.updatedAt = LocalDateTime.now()
    }
}