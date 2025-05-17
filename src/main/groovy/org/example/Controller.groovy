package org.example

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
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

    @GetMapping('/heartbeat')
    Map heartbeat() {
        return [version: '0.0.0']
    }

    @GetMapping('/health-check')
    Map healthCheck() {
        def result = 'success'
        try {
            restTemplate.getForEntity(pocketbaseUrl, String)
        } catch (HttpClientErrorException e) {
            if (e?.statusCode != HttpStatus.NOT_FOUND) {
                result = 'failure'
            }
        } catch (Exception ignored) {
            result = 'failure'
        }
        return [
                result      : result,
                integrations: [
                        [
                                name  : 'pocketbase',
                                result: result
                        ]
                ]
        ]
    }
}
