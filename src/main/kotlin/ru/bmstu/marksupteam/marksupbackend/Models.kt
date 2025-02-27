package ru.bmstu.marksupteam.marksupbackend

import jakarta.persistence.*
import kotlinx.datetime.*
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters


@Entity
data class Class(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    val discipline: Discipline,

    @ManyToOne
    val teacher: Teacher,

    @ManyToMany
    val students: List<Student>,

    @Convert(converter = LocalDateTimeConverter::class)
    val datetimeStart: LocalDateTime,

    @Convert(converter = LocalDateTimeConverter::class)
    val datetimeEnd: LocalDateTime,

    @OneToMany
    val assignmentsDue: List<Assignment>
)

@Entity
data class Assignment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    val student: Student,

    @ManyToOne
    val teacher: Teacher,

    @ManyToOne
    val discipline: Discipline,

    @Convert(converter = LocalDateConverter::class)
    val issuedOn: LocalDate,

    @Convert(converter = LocalDateConverter::class)
    val deadline: LocalDate,

    val description: String,

    @Enumerated(EnumType.STRING)
    val status: AssignmentStatus,

    val grade: Int?
)

@Entity
data class FavouritesItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    val profile: Profile,

    @ManyToOne
    val assignment: Assignment?,

    @ManyToOne
    val classObj: Class?
) {
    init {
        if (assignment == null && classObj == null) {
            throw IllegalArgumentException("At least one should be non-null!")
        }
    }
}

@Entity
data class Person(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String,

    val surname: String,

    val email: String,

    val phone: String,

    val imgUrl: String
)

@Entity
data class Discipline(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String,

    val complexity: Int
)

@Entity
data class Teacher(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    val person: Person,

    @ManyToMany
    val disciplines: List<Discipline>,

    val description: String
)

@Entity
data class Parent(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    val person: Person,

    @ManyToMany
    val children: List<Student>
)

@Entity
data class Student(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    val person: Person,

    val description: String
)

@Entity
data class Profile(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val username: String,

    @OneToOne
    val parent: Parent? = null,

    @OneToOne
    val student: Student? = null,

    @OneToOne
    val teacher: Teacher? = null
) {
    init {
        if (student == null && teacher == null && parent == null) {
            throw IllegalArgumentException("At least one should be non-null!")
        }
    }

}

enum class AssignmentStatus {
    Assigned, Completed, Defended
}

@Converter(autoApply = true)
class LocalDateConverter : AttributeConverter<LocalDate, java.time.LocalDate> {
    override fun convertToDatabaseColumn(attribute: LocalDate?): java.time.LocalDate? {
        return attribute?.toJavaLocalDate()
    }

    override fun convertToEntityAttribute(dbData: java.time.LocalDate?): LocalDate? {
        return dbData?.toKotlinLocalDate()
    }
}

@Converter(autoApply = true)
class LocalDateTimeConverter : AttributeConverter<LocalDateTime, java.time.LocalDateTime> {
    override fun convertToDatabaseColumn(attribute: LocalDateTime?): java.time.LocalDateTime? {
        return attribute?.toJavaLocalDateTime()
    }

    override fun convertToEntityAttribute(dbData: java.time.LocalDateTime?): LocalDateTime? {
        return dbData?.toKotlinLocalDateTime()
    }
}