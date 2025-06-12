package org.example

import jakarta.annotation.Resource
import org.springframework.stereotype.Service

@Service
class PocketbaseHealthCheck {
    final String SUCCESS = 'SUCCESS'
    final String FAILURE = 'FAILURE'
    final String NAME = 'Pocketbase'
    final String FAILURE_MESSAGE = 'Pocketbase failed to provide expected JWT'

    @Resource
    PocketbaseClient pocketbaseClient

    Map check() {
        def status = FAILURE
        def message = FAILURE_MESSAGE

        try {
            def response = pocketbaseClient.authWithPassword()
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
