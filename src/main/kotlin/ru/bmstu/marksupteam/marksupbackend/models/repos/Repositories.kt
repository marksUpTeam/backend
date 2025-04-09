package ru.bmstu.marksupteam.marksupbackend.models.repos

import org.springframework.data.jpa.repository.JpaRepository
import ru.bmstu.marksupteam.marksupbackend.models.Assignment
import ru.bmstu.marksupteam.marksupbackend.models.Class
import ru.bmstu.marksupteam.marksupbackend.models.FavouritesItem
import ru.bmstu.marksupteam.marksupbackend.models.Profile
import ru.bmstu.marksupteam.marksupbackend.models.Student
import ru.bmstu.marksupteam.marksupbackend.models.Teacher
import ru.bmstu.marksupteam.marksupbackend.models.VKIntegrationProfile
import java.util.Optional

interface ClassRepository : JpaRepository<Class, Long> {
    fun findAllByTeacher(teacher: Teacher): List<Class>
    fun findAllByStudent(student: Student): List<Class>
}
interface AssignmentRepository : JpaRepository<Assignment, Long> {
    fun findByStudent(student: Student): List<Assignment>
    fun findByTeacher(teacher: Teacher): List<Assignment>
}
interface FavouritesItemRepository : JpaRepository<FavouritesItem, Long> {
    fun findByProfile_Id(profileId: Long): List<FavouritesItem>
}
interface ProfileRepository : JpaRepository<Profile, Long>
interface VKIntegrationRepository: JpaRepository<VKIntegrationProfile, Long> {
    fun getVKIntegrationProfileByIdentifier(identifier: String): Optional<VKIntegrationProfile>
}
interface StudentRepository : JpaRepository<Student, Long>
interface TeacherRepository : JpaRepository<Teacher, Long>