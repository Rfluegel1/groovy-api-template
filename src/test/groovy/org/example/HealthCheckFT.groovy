package org.example

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Application)
class HealthCheckFT extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    def "health check returns app integrations"() {
        given:
        def expectedResult = 'success'
        if (System.getProperty('environment') == 'ci') {
            expectedResult = 'failure'
        }

        when:
        def response = restTemplate.getForEntity("/health-check", Map)

        then:
        response.statusCode == HttpStatus.OK

        response.body.result == expectedResult
        response.body.integrations.size() == 1
        response.body.integrations.find({it.name == 'pocketbase'}).result == expectedResult
    }
}