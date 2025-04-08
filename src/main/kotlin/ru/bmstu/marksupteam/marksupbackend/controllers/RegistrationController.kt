package ru.bmstu.marksupteam.marksupbackend.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.bmstu.marksupteam.marksupbackend.models.Profile
import ru.bmstu.marksupteam.marksupbackend.models.VKIntegrationProfile
import ru.bmstu.marksupteam.marksupbackend.services.ProfileService
import ru.bmstu.marksupteam.marksupbackend.services.VKIntegrationService

@RestController
@RequestMapping("/api/registration")
class RegistrationController(val profileService: ProfileService, val vkIntegrationService: VKIntegrationService) {
    @PostMapping
    fun postNewProfile(@RequestBody profile: Profile): ResponseEntity<Profile> {
        val id = SecurityContextHolder.getContext().authentication.name as String

        val vkIntegrationProfile = vkIntegrationService.getProfileByIdentifier(id)

        if (vkIntegrationProfile != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(profile)
        }

        val newAuthProfile = VKIntegrationProfile(
            profile = profile,
            identifier = id,
        )

        vkIntegrationService.save(newAuthProfile)

        return ResponseEntity.ok(profile)
    }
}