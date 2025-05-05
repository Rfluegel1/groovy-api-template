package org.example

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {

    @GetMapping('/heartbeat')
    Map heartbeat() {
        return [version: '0.0.0']
    }
}
