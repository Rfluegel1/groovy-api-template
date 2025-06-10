package org.example

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.info.BuildProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HeartbeatController {

    @Autowired(required = false)
    BuildProperties buildProperties

    @GetMapping('/heartbeat')
    Map heartbeat() {
        return [version: buildProperties?.version?.replace('-SNAPSHOT', '') ?: 'unknown']
    }
}
