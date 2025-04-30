package org.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Application {
    static void main(String[] args) {
        println "Hello world!"
        SpringApplication.run(Application, args)
    }
}