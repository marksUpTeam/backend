package ru.bmstu.marksupteam.marksupbackend.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.bmstu.marksupteam.marksupbackend.models.Invitation
import ru.bmstu.marksupteam.marksupbackend.models.InvitationType
import ru.bmstu.marksupteam.marksupbackend.models.Profile
import ru.bmstu.marksupteam.marksupbackend.models.VKIntegrationProfile
import ru.bmstu.marksupteam.marksupbackend.models.mapToInvitationType
import ru.bmstu.marksupteam.marksupbackend.services.InvitationService
import ru.bmstu.marksupteam.marksupbackend.services.ProfileService
import ru.bmstu.marksupteam.marksupbackend.services.VKIntegrationService

@RestController
@RequestMapping("/api/registration")
class RegistrationController(val profileService: ProfileService, val vkIntegrationService: VKIntegrationService, val invitationService: InvitationService) {
    @GetMapping()
    fun getInvitation(): ResponseEntity<Invitation> {
        val id = SecurityContextHolder.getContext().authentication.name as String
        val invite = invitationService.getInvitationByIdentifier(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(invite)

    }
    @PostMapping
    fun postNewProfile(@RequestBody profile: Profile): ResponseEntity<Profile> {
        val id = SecurityContextHolder.getContext().authentication.name as String
        val invite = invitationService.getInvitationByIdentifier(id) ?: return ResponseEntity.notFound().build()
        val inviteType = mapToInvitationType(invite)
        when (inviteType) {
            InvitationType.Parent -> {
                if (profile.teacher != null || profile.student != null) {
                    return ResponseEntity.badRequest().build()
                }
            }
            InvitationType.Student -> {
                if (profile.teacher != null || profile.parent != null) {
                    return ResponseEntity.badRequest().build()
                }
            }
            InvitationType.Teacher -> {
                if (profile.parent != null || profile.student != null) {
                    return ResponseEntity.badRequest().build()
                }
            }
        }

        val vkIntegrationProfile = vkIntegrationService.getProfileByIdentifier(id)

        if (vkIntegrationProfile != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(profile)
        }

        val newAuthProfile = VKIntegrationProfile(
            profile = profile,
            identifier = id,
        )

        vkIntegrationService.save(newAuthProfile)

        invitationService.deleteByIdentifier(id)

        return ResponseEntity.ok(profile)
    }
}