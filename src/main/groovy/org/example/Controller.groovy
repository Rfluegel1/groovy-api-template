package org.example

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@RestController
class Controller {

    @Autowired
    RestTemplate restTemplate

    @Value('${pocketbase-base-url}')
    String pocketbaseUrl

    @Value('${POCKETBASE_ENV_PASSWORD}')
    String pocketbasePassword

    @Value('${app.version}')
    String appVersion

    @GetMapping('/heartbeat')
    Map heartbeat() {
        return [version: appVersion]
    }

    @GetMapping('/health-check')
    Map healthCheck() {
        def result = 'failure'
        def message = ''
        try {
            def headers = new HttpHeaders()
            headers.setContentType(MediaType.APPLICATION_JSON)

            def body = [
                    identity: 'groovy-api-template@fake.com',
                    password: pocketbasePassword
            ]

            def request = new HttpEntity<>(body, headers)

            def response = restTemplate.postForEntity(
                    "${pocketbaseUrl}/api/collections/users/auth-with-password",
                    request,
                    Map.class
            )

            println(response)
            if (response.body.token) {
                result = 'success'
            }
        } catch (Exception ignored) {
            println(ignored)
            message = ignored.message
        }
        return [
                result      : result,
                integrations: [
                        [
                                name  : 'pocketbase',
                                result: result,
                                message: message
                        ]
                ]
        ]
    }
}
