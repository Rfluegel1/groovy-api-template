package org.example

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class PocketbaseHealthCheckTest extends Specification {
        def pocketbaseHealthCheck = new PocketbaseHealthCheck()
        def mockPocketbaseClient = Mock(PocketbaseClient)

    def setup() {
        pocketbaseHealthCheck.pocketbaseClient = mockPocketbaseClient
    }

    def 'succeeds when auth token is received'() {
        when:
        def result = pocketbaseHealthCheck.check()

        then:
        1 * pocketbaseHealthCheck.pocketbaseClient.authWithPassword() >> new ResponseEntity([token: 'your-auth-token'], HttpStatus.OK)
        result.name == 'Pocketbase'
        result.status == 'SUCCESS'
        result.message == ''
    }

    def 'fails when auth token is not provided'() {
        when:
        def result = pocketbaseHealthCheck.check()

        then:
        1 * pocketbaseHealthCheck.pocketbaseClient.authWithPassword() >> { new ResponseEntity([:], HttpStatus.OK) }
        result.name == 'Pocketbase'
        result.status == 'FAILURE'
        result.message == 'Pocketbase failed to provide expected JWT'
    }

    def 'fails when error is thrown'() {
        when:
        def result = pocketbaseHealthCheck.check()

        then:
        1 * pocketbaseHealthCheck.pocketbaseClient.authWithPassword() >> { throw new ConnectException('error message') }
        result.name == 'Pocketbase'
        result.status == 'FAILURE'
        result.message == 'error message'
    }
}
