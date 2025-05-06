package org.example

import groovy.json.JsonSlurper
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

class ControllerTest extends Specification {

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new Controller()).build()

    def "should return heartbeat version"() {
        when:
        MvcResult result = mockMvc.perform(get("/heartbeat")).andReturn()

        then:
        def json = new JsonSlurper().parseText(result.response.contentAsString)
        json.version == '0.0.0'
    }
}
