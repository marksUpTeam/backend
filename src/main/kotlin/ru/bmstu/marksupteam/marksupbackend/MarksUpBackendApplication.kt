package ru.bmstu.marksupteam.marksupbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MarksUpBackendApplication

fun main(args: Array<String>) {
    runApplication<MarksUpBackendApplication>(*args)
}
