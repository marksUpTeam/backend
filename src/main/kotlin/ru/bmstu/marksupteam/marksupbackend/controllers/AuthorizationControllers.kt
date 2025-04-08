package ru.bmstu.marksupteam.marksupbackend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.bmstu.marksupteam.marksupbackend.models.Profile
import ru.bmstu.marksupteam.marksupbackend.services.ProfileService
import ru.bmstu.marksupteam.marksupbackend.services.VKIntegrationService

@RestController
@RequestMapping("/api/user")
class ProfileController(private val vkIntegrationService: VKIntegrationService, private val profileService: ProfileService) {

    fun getByIdentifier(identifier: String): ResponseEntity<Profile>{
        val vkIntegrationProfile = vkIntegrationService.getProfileByIdentifier(identifier) ?: return ResponseEntity.notFound().build()
        val profile = vkIntegrationProfile.profile
        return ResponseEntity.ok(profile)
    }

    @GetMapping()
    fun getProfileResolveIdAuto(): ResponseEntity<Profile>{
        val auth = SecurityContextHolder.getContext().authentication
        return getByIdentifier(auth.name)
    }

    @PostMapping()
    fun postProfileResolveIdAuto(@RequestBody profile: Profile): ResponseEntity<String>{
        try {
            profileService.modifyProfile(profile)
            return ResponseEntity.ok("Profile ${profile.id} updated")
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().build()
        }
    }
}

@RestController
@RequestMapping("/api")
class ServiceController(private val vkIntegrationService: VKIntegrationService){
    @GetMapping
    fun testConnection(): ResponseEntity<String>{
        val auth = SecurityContextHolder.getContext().authentication
        vkIntegrationService.getProfileByIdentifier(auth.name) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok("\"exists\"")
    }
}