package org.example

import jakarta.annotation.Resource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {
    @Resource
    PocketbaseHealthCheck pocketbaseHealthCheck

    @GetMapping('/health-check')
    Map healthCheck() {
        def pocketbaseCheck = pocketbaseHealthCheck.check()
        return [
                result      : pocketbaseCheck.status,
                integrations: [
                        [
                                name  : pocketbaseCheck.name,
                                result: pocketbaseCheck.status,
                                message: pocketbaseCheck.message
                        ]
                ]
        ]
    }
}
