package ru.bmstu.marksupteam.marksupbackend

import org.springframework.stereotype.Service

@Service
class ClassService(private val classRepository: ClassRepository) {
    fun getAllClasses(): List<Class> = classRepository.findAll()
    fun getClassById(id: Long): Class? = classRepository.findById(id).orElse(null)
    fun getClassesByTeacher(teacher: Teacher): List<Class> = classRepository.findAllByTeacher(teacher)
    fun getClassesByStudent(student: Student): List<Class> = classRepository.findAllByStudent(student)
}

@Service
class AssignmentService(private val assignmentRepository: AssignmentRepository) {
    fun getAllAssignments(): List<Assignment> = assignmentRepository.findAll()
    fun getAssignmentById(id: Long): Assignment? = assignmentRepository.findById(id).orElse(null)
}

@Service
class FavouritesItemService(private val favouritesItemRepository: FavouritesItemRepository) {
    fun getAllFavouritesItems(): List<FavouritesItem> = favouritesItemRepository.findAll()
    fun getFavouritesItemById(id: Long): FavouritesItem? = favouritesItemRepository.findById(id).orElse(null)
}

@Service
class ProfileService(private val profileRepository: ProfileRepository) {
    fun getAllProfiles(): List<Profile> = profileRepository.findAll()
    fun getProfileById(id: Long): Profile? = profileRepository.findById(id).orElse(null)
}