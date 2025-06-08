package org.example

import jakarta.annotation.Resource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.info.BuildProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {

    @Autowired(required = false)
    BuildProperties buildProperties

    @Resource
    PocketbaseHealthCheck pocketbaseHealthCheck

    @GetMapping('/heartbeat')
    Map heartbeat() {
        return [version: buildProperties?.version ?: 'unknown']
    }

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
