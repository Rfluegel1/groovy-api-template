package org.example

import groovy.json.JsonSlurper
import org.springframework.boot.info.BuildProperties
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

class HeartbeatControllerTest extends Specification {
    def controller = new HeartbeatController()

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build()

    def setup() {
        def mockBuildProperties = Mock(BuildProperties) {
            getVersion() >> '0.0.0-SNAPSHOT'
        }
        controller.buildProperties = mockBuildProperties
    }

    def "displays version without pre-release labels "() {
        when:
        MvcResult result = mockMvc.perform(get("/heartbeat")).andReturn()

        then:
        def json = new JsonSlurper().parseText(result.response.contentAsString)
        json.version == '0.0.0'
    }
}