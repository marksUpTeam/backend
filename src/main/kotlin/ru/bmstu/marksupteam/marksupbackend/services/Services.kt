package ru.bmstu.marksupteam.marksupbackend.services

import org.hibernate.exception.DataException
import org.springframework.stereotype.Service
import ru.bmstu.marksupteam.marksupbackend.models.Assignment
import ru.bmstu.marksupteam.marksupbackend.models.repos.AssignmentRepository
import ru.bmstu.marksupteam.marksupbackend.models.Class
import ru.bmstu.marksupteam.marksupbackend.models.repos.ClassRepository
import ru.bmstu.marksupteam.marksupbackend.models.FavouritesItem
import ru.bmstu.marksupteam.marksupbackend.models.repos.FavouritesItemRepository
import ru.bmstu.marksupteam.marksupbackend.models.Profile
import ru.bmstu.marksupteam.marksupbackend.models.repos.ProfileRepository
import ru.bmstu.marksupteam.marksupbackend.models.Student
import ru.bmstu.marksupteam.marksupbackend.models.Teacher
import ru.bmstu.marksupteam.marksupbackend.models.VKIntegrationProfile
import ru.bmstu.marksupteam.marksupbackend.models.repos.StudentRepository
import ru.bmstu.marksupteam.marksupbackend.models.repos.TeacherRepository
import ru.bmstu.marksupteam.marksupbackend.models.repos.VKIntegrationRepository

@Service
class ClassService(private val classRepository: ClassRepository) {
    fun getAllClasses(): List<Class> = classRepository.findAll()
    fun getClassById(id: Long): Class? = classRepository.findById(id).orElse(null)
    fun getClassesByTeacher(teacher: Teacher): List<Class> = classRepository.findAllByTeacher(teacher)
    fun getClassesByStudent(student: Student): List<Class> = classRepository.findAllByStudent(student)
    fun addClass(classEntry: Class): Class = classRepository.save(classEntry)
}

@Service
class AssignmentService(private val assignmentRepository: AssignmentRepository) {
    fun getAllAssignments(): List<Assignment> = assignmentRepository.findAll()
    fun getAssignmentById(id: Long): Assignment? = assignmentRepository.findById(id).orElse(null)
    fun getAssignmentsByTeacher(teacher: Teacher): List<Assignment> = assignmentRepository.findByTeacher(teacher)
    fun getAssignmentsByStudent(student: Student): List<Assignment> = assignmentRepository.findByStudent(student)
    fun addAssignment(assignment: Assignment): Assignment = assignmentRepository.save(assignment)
}

@Service
class FavouritesItemService(private val favouritesItemRepository: FavouritesItemRepository) {
    fun getAllFavouritesItems(): List<FavouritesItem> = favouritesItemRepository.findAll()
    fun getFavouritesItemById(id: Long): FavouritesItem? = favouritesItemRepository.findById(id).orElse(null)
    fun getFavouritesByProfileId(id: Long): List<FavouritesItem> = favouritesItemRepository.findByProfile_Id(id)
    fun addFavouriteItem(favItem: FavouritesItem): FavouritesItem = favouritesItemRepository.save(favItem)
}

@Service
class ProfileService(private val profileRepository: ProfileRepository) {
    fun getAllProfiles(): List<Profile> = profileRepository.findAll()
    fun getProfileById(id: Long): Profile? = profileRepository.findById(id).orElse(null)
    fun modifyProfile(profile: Profile) {
        if (profile.teacher != null){
            if (profile.teacher!!.assignedStudents.isEmpty()){
                throw Exception("Assigned students not filled")
            }
        }
        else if (profile.student != null){
            if (profile.student!!.assignedTeachers.isEmpty()){
                throw Exception("Assigned teachers not filled")
            }
        }
        profileRepository.save(profile)
    }
}

@Service
class VKIntegrationService(private val vkIntegrationRepository: VKIntegrationRepository) {
    fun getProfileByIdentifier(identifier: String): VKIntegrationProfile? = vkIntegrationRepository.getVKIntegrationProfileByIdentifier(identifier).orElse(null)
    fun save(vkIntegrationProfile: VKIntegrationProfile): VKIntegrationProfile = vkIntegrationRepository.save(vkIntegrationProfile)
}

@Service
class StudentService(private val studentRepository: StudentRepository){
    fun getStudentById(id: Long): Student = studentRepository.findById(id).orElse(null)
}

@Service
class TeacherService(private val teacherRepository: TeacherRepository){
    fun getTeacherById(id: Long): Teacher = teacherRepository.findById(id).orElse(null)
}