package ru.bmstu.marksupteam.marksupbackend

import org.springframework.data.jpa.repository.JpaRepository

interface ClassRepository : JpaRepository<Class, Long> {
    fun findAllByTeacher(teacher: Teacher): List<Class>
    fun findAllByStudent(student: Student): List<Class>
}
interface AssignmentRepository : JpaRepository<Assignment, Long>
interface FavouritesItemRepository : JpaRepository<FavouritesItem, Long>
interface ProfileRepository : JpaRepository<Profile, Long>