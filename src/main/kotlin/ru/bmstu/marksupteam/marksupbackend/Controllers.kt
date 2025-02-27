package ru.bmstu.marksupteam.marksupbackend

import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/api/classes")
class ClassController(private val classService: ClassService, private val profileService: ProfileService){
    @GetMapping("/{idProfile}")
    fun getClassesByProfileId(@PathVariable idProfile: Long): ResponseEntity<List<Class>>{
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
}

@Controller
@RequestMapping("/api/assignments")
class AssignmentsController(private val assignmentService: AssignmentService, private val profileService: ProfileService) {
    @GetMapping("/{idProfile}")
    fun getAssignmentsByProfileId(@PathVariable idProfile: Long): ResponseEntity<List<Assignment>>{
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
}

@Controller
@RequestMapping("/api/user/favourites")
class FavouritesController(private val favouritesItemService: FavouritesItemService){

}

@Controller
@RequestMapping("/api/user")
class ProfileController(private val profileService: ProfileService){

}