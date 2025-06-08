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

    @Value('${pocketbase-base-url}')
    String pocketbaseUrl

    @Value('${POCKETBASE_ENV_PASSWORD}')
    String pocketbasePassword

    Map check() {
        def status = 'FAILURE'
        def message = 'Pocketbase failed to provide expected JWT'

        def headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)

        def body = [
                identity: 'groovy-api-template@fake.com',
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
                status = 'SUCCESS'
                message = ''
            }
        } catch (Exception e) {
            message = e.message
        }

        return [
                name   : 'Pocketbase',
                status : status,
                message: message
        ]
    }
}
