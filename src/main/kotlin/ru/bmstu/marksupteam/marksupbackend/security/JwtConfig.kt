package ru.bmstu.marksupteam.marksupbackend.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Configuration
class JwtConfig {

    @Value("\${jwtKey}")
    private lateinit var jwtKeyPEM: String

    @Bean
    fun publicKey(): PublicKey {
        val publicKeyContent = jwtKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replace("\n", "").trim()
        val keyBytes = Base64.getDecoder().decode(publicKeyContent)
        val keySpec = X509EncodedKeySpec(keyBytes)
        return KeyFactory.getInstance("RSA").generatePublic(keySpec)
    }
}