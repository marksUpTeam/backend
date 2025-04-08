package ru.bmstu.marksupteam.marksupbackend.controllers

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.bmstu.marksupteam.marksupbackend.services.ProfileService
import ru.bmstu.marksupteam.marksupbackend.services.VKIntegrationService

@RestController
@RequestMapping("/api/registration")
class RegistrationController(val profileService: ProfileService, val vkIntegrationService: VKIntegrationService) {
    
}