package ru.bmstu.marksupteam.marksupbackend

import org.springframework.data.jpa.repository.JpaRepository

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
    fun getVKIntegrationProfileByIdentifier(identifier: String): VKIntegrationProfile
}