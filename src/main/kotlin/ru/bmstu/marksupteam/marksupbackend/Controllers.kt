package ru.bmstu.marksupteam.marksupbackend

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/api/classes")
class ClassController(private val classService: ClassService){
    @GetMapping
    fun getClassByTeacher(@RequestBody teacher: Teacher): ResponseEntity<List<Class>>{
        val response = classService.getClassesByTeacher(teacher)
        if (response.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(listOf())
        }
        return ResponseEntity.ok(response)
    }
}

@Controller
@RequestMapping("/api/assignments")
class AssignmentsController(private val assignmentService: AssignmentService){

}

@Controller
@RequestMapping("/api/user/favourites")
class FavouritesController(private val favouritesItemService: FavouritesItemService){

}

@Controller
@RequestMapping("/api/user")
class ProfileController(private val profileService: ProfileService){

}