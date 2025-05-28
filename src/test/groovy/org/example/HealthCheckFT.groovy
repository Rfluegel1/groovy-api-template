package org.example

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = Application)
class HealthCheckFT extends Specification {

    @Value('${base.url}')
    String baseUrl

    def restTemplate = new TestRestTemplate()

    def "health check returns app integrations"() {
        when:
        def response = restTemplate.getForEntity("${baseUrl}/health-check", Map)

        then:
        response.statusCode == HttpStatus.OK
        response.body.result == 'success'
        response.body.integrations.find({ it.name == 'pocketbase' }).result == 'success'
    }
}