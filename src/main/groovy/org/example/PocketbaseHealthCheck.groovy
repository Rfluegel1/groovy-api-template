package org.example

import jakarta.annotation.Resource
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class PocketbaseHealthCheck {

    @Resource
    RestTemplate restTemplate

    @Value('${pocketbase.base.url}')
    String pocketbaseUrl

    @Value('${POCKETBASE_ENV_PASSWORD}')
    String pocketbasePassword

    final String SUCCESS = 'SUCCESS'
    final String FAILURE = 'FAILURE'
    final String NAME = 'Pocketbase'
    final String IDENTITY = 'groovy-api-template@fake.com'
    final String FAILURE_MESSAGE = 'Pocketbase failed to provide expected JWT'

    Map check() {
        def status = FAILURE
        def message = FAILURE_MESSAGE
        def headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)

        def body = [
                identity: IDENTITY,
                password: pocketbasePassword
        ]

        def request = new HttpEntity<>(body, headers)

        try {
            def response = restTemplate.postForEntity(
                    "${pocketbaseUrl}/api/collections/users/auth-with-password",
                    request,
                    Map.class
            )

            if (response.body.token) {
                status = SUCCESS
                message = ''
            }
        } catch (Exception e) {
            message = e.message
        }

        return [
                name   : NAME,
                status : status,
                message: message
        ]
    }
}
