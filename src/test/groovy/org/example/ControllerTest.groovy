package org.example

import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
        controller.pocketbaseUrl = 'http://127.0.0.1:8090'
        controller.pocketbasePassword = 'testPassword@'
    }

    def "should return heartbeat version"() {
        when:
        MvcResult result = mockMvc.perform(get("/heartbeat")).andReturn()

        then:
        def json = new JsonSlurper().parseText(result.response.contentAsString)
        json.version == '0.0.0'
    }

    def 'pocketbase integration health check is result: #expectedResult for exception: #exception'() {
        when:
        MvcResult result = mockMvc.perform(get("/health-check")).andReturn()

        then:
        1 * controller.restTemplate.postForEntity(*_) >> { throw exception }
        def json = new JsonSlurper().parseText(result.response.contentAsString)
        json.result == expectedResult
        json.integrations.find({ it.name == 'pocketbase' }).result == expectedResult
        json.integrations.find({ it.name == 'pocketbase' }).message == 'error message'

        where:
        expectedResult | exception
        'failure'      | new ConnectException('error message')
    }

    def 'pocketbase integration health check is success when authenticated'() {
        when:
        MvcResult result = mockMvc.perform(get("/health-check")).andReturn()

        then:
        1 * controller.restTemplate.postForEntity(
                'http://127.0.0.1:8090/api/collections/users/auth-with-password',
                { it.body.identity == 'groovy-api-template@fake.com' && it.body.password == 'testPassword@' },
                Map
        ) >> new ResponseEntity([token: 'your-auth-token'], HttpStatus.OK)
        def json = new JsonSlurper().parseText(result.response.contentAsString)
        json.result == 'success'
        json.integrations.find({ it.name == 'pocketbase' }).result == 'success'
        json.integrations.find({ it.name == 'pocketbase' }).message == ''
    }
}