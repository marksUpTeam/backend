package ru.bmstu.marksupteam.marksupbackend

import org.apache.tomcat.util.http.parser.Authorization
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.security.AuthProvider


@Controller
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
        return getClassesByProfileId(profile.id)
    }
}

@Controller
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
        return getAssignmentsByProfileId(profile.id)
    }
}

@Controller
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

}

@Controller
@RequestMapping("/api/user")
class ProfileController(private val vkIntegrationService: VKIntegrationService){
    @GetMapping("/{identifier}")
    fun getByIdentifier(@PathVariable identifier: String): ResponseEntity<Profile>{
        val vkIntegrationProfile = vkIntegrationService.getProfileByIdentifier(identifier) ?: return ResponseEntity.notFound().build()
        val profile = vkIntegrationProfile.profile
        return ResponseEntity.ok(profile)
    }
}

@Controller
@RequestMapping("/api")
class ServiceController(){
    @GetMapping
    fun testConnection(): ResponseEntity<String>{
        val auth = SecurityContextHolder.getContext().authentication
        return ResponseEntity.ok(auth.name)
    }
}