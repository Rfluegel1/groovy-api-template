package org.example

import groovy.json.JsonSlurper
import org.springframework.boot.info.BuildProperties
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

class ControllerTest extends Specification {
    def controller = new Controller()

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build()

    def setup() {
        def mockBuildProperties = Mock(BuildProperties) {
            getVersion() >> '0.0.0'
        }
        controller.buildProperties = mockBuildProperties
        def mockPocketbaseHealthCheck = Mock(PocketbaseHealthCheck) {
            check() >> [name: 'pocketbase', status: 'SUCCESS', message: '']
        }
        controller.pocketbaseHealthCheck = mockPocketbaseHealthCheck
    }

    def "should return heartbeat version"() {
        when:
        MvcResult result = mockMvc.perform(get("/heartbeat")).andReturn()

        then:
        def json = new JsonSlurper().parseText(result.response.contentAsString)
        json.version == '0.0.0'
    }

    def 'calls for pocketbase healthcheck'() {
        when:
        MvcResult result = mockMvc.perform(get("/health-check")).andReturn()

        then:
        1 * controller.pocketbaseHealthCheck.check() >> [name: 'pocketbase', status: 'SUCCESS', message: '']
        def json = new JsonSlurper().parseText(result.response.contentAsString)
        json.result == 'SUCCESS'
        json.integrations.find({ it.name == 'pocketbase' }).result == 'SUCCESS'
        json.integrations.find({ it.name == 'pocketbase' }).message == ''
    }
}