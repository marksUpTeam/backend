package ru.bmstu.marksupteam.marksupbackend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.bmstu.marksupteam.marksupbackend.models.Assignment
import ru.bmstu.marksupteam.marksupbackend.models.Class
import ru.bmstu.marksupteam.marksupbackend.models.FavouritesItem
import ru.bmstu.marksupteam.marksupbackend.models.Profile
import ru.bmstu.marksupteam.marksupbackend.services.AssignmentService
import ru.bmstu.marksupteam.marksupbackend.services.ClassService
import ru.bmstu.marksupteam.marksupbackend.services.FavouritesItemService
import ru.bmstu.marksupteam.marksupbackend.services.ProfileService
import ru.bmstu.marksupteam.marksupbackend.services.VKIntegrationService


@RestController
@RequestMapping("/api/classes")
class ClassController(private val classService: ClassService, private val profileService: ProfileService, private val vkIntegrationService: VKIntegrationService){
    fun getClassesByProfileId(idProfile: Long): ResponseEntity<List<Class>>{
        val profile = profileService.getProfileById(idProfile) ?: return ResponseEntity.notFound().build()
        when {
            (profile.student != null) -> {
                val classes: List<Class> = classService.getClassesByStudent(profile.student!!)
                return ResponseEntity.ok(classes)
            }
            (profile.teacher != null) -> {
                val classes: List<Class> = classService.getClassesByTeacher(profile.teacher!!)
                return ResponseEntity.ok(classes)
            }
            else -> return ResponseEntity.badRequest().build()
        }
    }

    @GetMapping
    fun getClassesResolveIdAuto(): ResponseEntity<List<Class>>{
        val vkId = SecurityContextHolder.getContext().authentication.name
        val vkIntegrationProfile = vkIntegrationService.getProfileByIdentifier(vkId)
        val profile = vkIntegrationProfile?.profile ?: return ResponseEntity.notFound().build()
        if (profile.parent != null) {
            return ResponseEntity.ok(classService.getClassesByStudent(profile.parent!!.currentChild))
        }
        return getClassesByProfileId(profile.id)
    }

    @PostMapping
    fun addClass(@RequestBody classEntry: Class): ResponseEntity<Class> {
        runCatching {
            return ResponseEntity.ok(classService.addClass(classEntry))
        }.onFailure {
            return ResponseEntity.internalServerError().build()
        }
        return ResponseEntity.badRequest().build()
    }

}

@RestController
@RequestMapping("/api/assignments")
class AssignmentsController(private val assignmentService: AssignmentService, private val profileService: ProfileService, private val vkIntegrationService: VKIntegrationService) {
    fun getAssignmentsByProfileId(idProfile: Long): ResponseEntity<List<Assignment>>{
        val profile = profileService.getProfileById(idProfile) ?: return ResponseEntity.notFound().build()
        when {
            (profile.student != null) -> {
                val assignments: List<Assignment> = assignmentService.getAssignmentsByStudent(profile.student!!)
                return ResponseEntity.ok(assignments)
            }
            (profile.teacher != null) -> {
                val assignments: List<Assignment> = assignmentService.getAssignmentsByTeacher(profile.teacher!!)
                return ResponseEntity.ok(assignments)
            }
            else -> return ResponseEntity.badRequest().build()
        }
    }
    @GetMapping()
    fun getAssignmentsResolveIdAuto(): ResponseEntity<List<Assignment>>{
        val vkId = SecurityContextHolder.getContext().authentication.name
        val vkIntegrationProfile = vkIntegrationService.getProfileByIdentifier(vkId)
        val profile = vkIntegrationProfile?.profile ?: return ResponseEntity.notFound().build()
        if (profile.parent != null) {
            return ResponseEntity.ok(assignmentService.getAssignmentsByStudent(profile.parent!!.currentChild))
        }
        return getAssignmentsByProfileId(profile.id)
    }

    @PostMapping
    fun addAssignment(@RequestBody assignment: Assignment): ResponseEntity<Assignment> {
        runCatching {
            return ResponseEntity.ok(assignmentService.addAssignment(assignment))
        }.onFailure {
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.badRequest().build()
    }
}

@RestController
@RequestMapping("/api/user/favourites")
class FavouritesController(private val favouritesItemService: FavouritesItemService, private val vkIntegrationService: VKIntegrationService) {

    fun getFavourites(idProfile: Long): ResponseEntity<List<FavouritesItem>>{
        val list = favouritesItemService.getFavouritesByProfileId(idProfile)
        if (list.isEmpty()){
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(list)
    }

    @GetMapping
    fun getFavouritesResolveIdAuto(): ResponseEntity<List<FavouritesItem>>{
        val vkId = SecurityContextHolder.getContext().authentication.name
        val vkIntegrationProfile = vkIntegrationService.getProfileByIdentifier(vkId)
        val profile = vkIntegrationProfile?.profile ?: return ResponseEntity.notFound().build()
        return getFavourites(profile.id)
    }

    @PostMapping
    fun addFavouritesItem(@RequestBody favItem: FavouritesItem): ResponseEntity<FavouritesItem> {
        runCatching {
            return ResponseEntity.ok(favouritesItemService.addFavouriteItem(favItem))
        }.onFailure {
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.badRequest().build()
    }

}



@RestController
@RequestMapping("/api/students")
class StudentsController(private val vkIntegrationService: VKIntegrationService, private val profileService: ProfileService) {}