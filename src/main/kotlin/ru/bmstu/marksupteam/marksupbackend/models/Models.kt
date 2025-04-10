package ru.bmstu.marksupteam.marksupbackend.models

import jakarta.persistence.*
import kotlinx.datetime.*
import net.minidev.json.annotate.JsonIgnore


@Entity
data class Class(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    val discipline: Discipline,

    @ManyToOne
    val teacher: Teacher,

    @ManyToOne
    val student: Student,

    @Convert(converter = LocalDateTimeConverter::class)
    val datetimeStart: LocalDateTime,

    @Convert(converter = LocalDateTimeConverter::class)
    val datetimeEnd: LocalDateTime,

    @ManyToOne
    val assignmentsDue: Assignment,

    val teacherComment: String
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
data class DisciplineGrade(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne
    val discipline: Discipline,
    val grade: Float
)

@Entity
data class Teacher(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    val person: Person,

    @ManyToMany
    val disciplines: List<Discipline>,

    val description: String,

    @JsonIgnore
    @ManyToMany
    val assignedStudents: List<Student>
)

@Entity
data class Parent(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    val person: Person,

    @ManyToMany
    val children: List<Student>,

    @OneToOne
    val currentChild: Student
)

@Entity
data class Student(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    val person: Person,

    val description: String,

    @JsonIgnore
    @ManyToMany
    val assignedTeachers: List<Teacher>,

    @ManyToMany
    val disciplineGrades: List<DisciplineGrade>
)


@Entity
data class Profile(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val username: String,

    @OneToOne(cascade = [CascadeType.ALL])
    val parent: Parent? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    val student: Student? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    val teacher: Teacher? = null
) {
    init {
        if (student == null && teacher == null && parent == null) {
            throw IllegalArgumentException("At least one should be non-null!")
        }
    }

}

@Entity
data class Invitation(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true)
    val identifier: String,

    @OneToMany
    val childrenList: List<Student>? = null,

    @OneToMany
    val studentList: List<Student>? = null,

    @OneToMany
    val teacherList: List<Teacher>? = null

)

enum class InvitationType {
    Parent, Teacher, Student
}

fun mapToInvitationType(invitation: Invitation): InvitationType {
    if (invitation.childrenList != null) {
        return InvitationType.Parent
    }
    else if (invitation.studentList != null) {
        return InvitationType.Teacher
    }
    else if (invitation.teacherList != null) {
        return InvitationType.Student
    }
}

@Entity
data class VKIntegrationProfile(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne(cascade = [CascadeType.ALL])
    val profile: Profile,

    @Column(unique = true)
    val identifier: String
)

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