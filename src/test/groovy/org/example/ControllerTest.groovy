package org.example

import groovy.json.JsonSlurper
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

class ControllerTest extends Specification {
    def controller = new Controller()

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build()

    def setup() {
        controller.restTemplate = Mock(RestTemplate)
    }

    def "should return heartbeat version"() {
        when:
        MvcResult result = mockMvc.perform(get("/heartbeat")).andReturn()

        then:
        def json = new JsonSlurper().parseText(result.response.contentAsString)
        json.version == '0.0.0'
    }

    def 'successful pocketbase integration returns 404'() {
        when:
        MvcResult result = mockMvc.perform(get("/health-check")).andReturn()

        then:
        1 * controller.restTemplate.getForEntity('http://127.0.0.1:8090/', String)
                >> {throw new HttpClientErrorException.NotFound(null, null, null, null) }
        def json = new JsonSlurper().parseText(result.response.contentAsString)
        json.result == 'success'
        json.integrations.find({it.name == 'pocketbase'}).result == 'success'
    }

    def 'failure pocketbase integration returns non 404'() {
        when:
        MvcResult result = mockMvc.perform(get("/health-check")).andReturn()

        then:
        1 * controller.restTemplate.getForEntity('http://127.0.0.1:8090/', String)
                >> {throw new HttpClientErrorException.BadRequest(null, null, null, null) }
        def json = new JsonSlurper().parseText(result.response.contentAsString)
        json.result == 'failure'
        json.integrations.find({it.name == 'pocketbase'}).result == 'failure'
    }

    def 'failure pocketbase integration returns connection exception'() {
        when:
        MvcResult result = mockMvc.perform(get("/health-check")).andReturn()

        then:
        1 * controller.restTemplate.getForEntity('http://127.0.0.1:8090/', String)
                >> {throw new ConnectException() }
        def json = new JsonSlurper().parseText(result.response.contentAsString)
        json.result == 'failure'
        json.integrations.find({it.name == 'pocketbase'}).result == 'failure'
    }
}
