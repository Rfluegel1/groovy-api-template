package org.example

import org.springframework.boot.SpringApplication
import spock.lang.Specification

class ApplicationTest extends Specification {

    def 'main calls run'() {
        given:
        GroovyMock(SpringApplication, global: true)

        when:
        new Application().main('')

        then:
        1 * SpringApplication.run(Application, '')
    }
}
