package org.example

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Application)
class HeartbeatFT extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    def "heartbeat returns app version"() {
        when:
        def response = restTemplate.getForEntity("/heartbeat", Map)

        then:
        response.statusCode == HttpStatus.OK
        response.body.version == '0.0.0'
    }
}