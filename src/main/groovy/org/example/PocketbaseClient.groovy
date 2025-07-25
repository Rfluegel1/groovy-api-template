package org.example

import jakarta.annotation.Resource
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class PocketbaseClient {
    final String IDENTITY = 'groovy-api-template@fake.com'

    @Value('${pocketbase.base.url}')
    String pocketbaseUrl

    @Value('${POCKETBASE_ENV_PASSWORD}')
    String pocketbasePassword

    @Resource
    RestTemplate restTemplate

    Object authWithPassword() {
        def headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)

        def body = [
                identity: IDENTITY,
                password: pocketbasePassword
        ]

        def request = new HttpEntity<>(body, headers)

        return restTemplate.postForEntity(
                "${pocketbaseUrl}/api/collections/users/auth-with-password",
                request,
                Map.class
        )
    }
}
