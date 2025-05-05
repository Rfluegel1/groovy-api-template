package org.example

import spock.lang.Specification

class ControllerTest extends Specification {
    def 'heartbeat returns static version'() {
        when:
        def result = new Controller().heartbeat()

        then:
        result.version == '0.0.0'
    }
}
