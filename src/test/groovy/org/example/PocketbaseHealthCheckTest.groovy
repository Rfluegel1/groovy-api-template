package org.example

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class PocketbaseHealthCheckTest extends Specification {
        def pocketbaseHealthCheck = new PocketbaseHealthCheck()

    def setup() {
        pocketbaseHealthCheck.restTemplate = Mock(RestTemplate)
        pocketbaseHealthCheck.pocketbaseUrl = 'http://127.0.0.1:8090'
        pocketbaseHealthCheck.pocketbasePassword = 'testPassword@'
    }

    def 'succeeds when auth token is received'() {
        when:
        def result = pocketbaseHealthCheck.check()

        then:
        1 * pocketbaseHealthCheck.restTemplate.postForEntity(
                'http://127.0.0.1:8090/api/collections/users/auth-with-password',
                { it.body.identity == 'groovy-api-template@fake.com' && it.body.password == 'testPassword@' },
                Map
        ) >> new ResponseEntity([token: 'your-auth-token'], HttpStatus.OK)

        result.name == 'Pocketbase'
        result.status == 'SUCCESS'
        result.message == ''
    }

    def 'fails when auth token is not provided'() {
        when:
        def result = pocketbaseHealthCheck.check()

        then:
        1 * pocketbaseHealthCheck.restTemplate.postForEntity(*_) >> { new ResponseEntity([:], HttpStatus.OK) }
        result.name == 'Pocketbase'
        result.status == 'FAILURE'
        result.message == 'Pocketbase failed to provide expected JWT'
    }

    def 'fails when error is thrown'() {
        when:
        def result = pocketbaseHealthCheck.check()

        then:
        1 * pocketbaseHealthCheck.restTemplate.postForEntity(*_) >> { throw new ConnectException('error message') }
        result.name == 'Pocketbase'
        result.status == 'FAILURE'
        result.message == 'error message'
    }
}
