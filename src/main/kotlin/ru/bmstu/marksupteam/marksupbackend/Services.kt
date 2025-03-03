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
    fun getAssignmentsByTeacher(teacher: Teacher): List<Assignment> = assignmentRepository.findByTeacher(teacher)
    fun getAssignmentsByStudent(student: Student): List<Assignment> = assignmentRepository.findByStudent(student)
}

@Service
class FavouritesItemService(private val favouritesItemRepository: FavouritesItemRepository) {
    fun getAllFavouritesItems(): List<FavouritesItem> = favouritesItemRepository.findAll()
    fun getFavouritesItemById(id: Long): FavouritesItem? = favouritesItemRepository.findById(id).orElse(null)
    fun getFavouritesByProfileId(id: Long): List<FavouritesItem> = favouritesItemRepository.findByProfile_Id(id)
}

@Service
class ProfileService(private val profileRepository: ProfileRepository) {
    fun getAllProfiles(): List<Profile> = profileRepository.findAll()
    fun getProfileById(id: Long): Profile? = profileRepository.findById(id).orElse(null)
}

@Service
class VKIntegrationService(private val vkIntegrationRepository: VKIntegrationRepository) {
    fun getProfileByIdentifier(identifier: String): VKIntegrationProfile? = vkIntegrationRepository.getVKIntegrationProfileByIdentifier(identifier).orElse(null)
}