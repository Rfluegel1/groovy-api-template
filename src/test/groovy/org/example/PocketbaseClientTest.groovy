package org.example

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class PocketbaseClientTest extends Specification {

    def pocketbaseClient = new PocketbaseClient()

    def setup() {
        pocketbaseClient.restTemplate = Mock(RestTemplate)
        pocketbaseClient.pocketbaseUrl = 'http://127.0.0.1:8090'
        pocketbaseClient.pocketbasePassword = 'testPassword@'
    }

    def 'posts to authentication endpoint'() {
        given:
        def expected = new ResponseEntity([token: 'your-auth-token'], HttpStatus.OK)

        when:
        def actual = pocketbaseClient.authWithPassword()

        then:
        1 * pocketbaseClient.restTemplate.postForEntity(
                'http://127.0.0.1:8090/api/collections/users/auth-with-password',
                { it.body.identity == 'groovy-api-template@fake.com' && it.body.password == 'testPassword@' },
                Map
        ) >> expected

        actual == expected
    }
}
