package ru.bmstu.marksupteam.marksupbackend.security

import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.security.PublicKey

@Component
class JwtFilter(private val publicKey: PublicKey) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val authToken = authHeader.substring(7)
            try{
                val claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(authToken).body
                val expirationTime = claims.expiration.time
                if (expirationTime < System.currentTimeMillis()) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired")
                    return
                }
                val issuer = claims["iis"]
                val subject = claims.subject
                val appId = claims["app"]
                val jwtId = claims["jti"]

                if (issuer != "VK"){
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid issuer")
                    return
                }

                val authentication = UsernamePasswordAuthenticationToken(subject, null, emptyList())
                println("Authentication: $authentication")
                SecurityContextHolder.getContext().authentication = authentication
            } catch (e: Exception){
                SecurityContextHolder.clearContext()
                println("Exception: $e")
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token")
                return
            }
        } else {
            SecurityContextHolder.clearContext()
            println("Unauthorized")
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
            return
        }
        filterChain.doFilter(request, response)
    }
}