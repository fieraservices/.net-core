package com.fiera.api.Controller;

import com.fiera.api.dtos.UserDTO;
import com.fiera.api.exceptions.CustomBadRequestException;
import com.fiera.api.exceptions.CustomNotFoundException;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class UserControllerIT {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private UserDTO userDTO;

    private static HttpHeaders headers = new HttpHeaders();

    @BeforeAll
    public static void init() throws Exception
    {
        String accessToken = obtainAccessToken();
        headers.setBearerAuth(accessToken);
    }

    @BeforeEach
    public void setup() {
        userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setDocNumber("123456789");
        userDTO.setFirstName("Luke");
        userDTO.setLastName("Skywalker");
        userDTO.setEmail("jedi@email.com");
        userDTO.setPhone("987654321");
        userDTO.setAddress("123 death star avenue");
    }

    @Test
    public void shouldReturnAllUsers() {

        HttpEntity<String> request = new HttpEntity<String>(null, headers);

        ResponseEntity<UserDTO[]> actual = restTemplate.exchange(createURLWithPort("/users"), HttpMethod.GET, request,
                UserDTO[].class);

        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(200);
        then(actual.getBody()).as("Check if we have a result").isNotNull();
    }

    @Test
    void shouldReturnUserById() throws CustomNotFoundException {

        HttpEntity<String> request = new HttpEntity<String>(null, headers);

        ResponseEntity<UserDTO> actual = restTemplate.exchange(createURLWithPort("/users/1"), HttpMethod.GET, request,
        UserDTO.class);

        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(200);
        then(actual.getBody()).as("Check if we have a result").isNotNull();
    }

    @Test
    void badIdShouldReturnNotFound() throws CustomNotFoundException {
    
        HttpEntity<String> request = new HttpEntity<String>(null, headers);

        ResponseEntity<UserDTO> actual = restTemplate.exchange(createURLWithPort("/users/2"), HttpMethod.GET, request,
        UserDTO.class);

        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(404);
    }

    @Test
    void badIdShouldReturnBadRequest() throws CustomNotFoundException {
    
        HttpEntity<String> request = new HttpEntity<String>(null, headers);

        ResponseEntity<UserDTO> actual = restTemplate.exchange(createURLWithPort("/users/a"), HttpMethod.GET, request,
        UserDTO.class);

        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(400);        
    }

    @Test
    void shouldSaveUserSuccessFully() throws CustomBadRequestException {

        userDTO.setUserId(null);

        HttpEntity<UserDTO> request = new HttpEntity<UserDTO>(userDTO, headers);

        ResponseEntity<Object> actual = restTemplate.exchange(createURLWithPort("/users"), HttpMethod.POST, request,
        Object.class);
    
        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(201);
        then(actual.getHeaders().get("location").get(0)).as("Check if we have the expected location at the header").contains(createURLWithPort("/users/"));
    }

    @Test
    void badRequestShouldReturnException() throws CustomBadRequestException {

        HttpEntity<UserDTO> request = new HttpEntity<UserDTO>(userDTO, headers);

        ResponseEntity<Object> actual = restTemplate.exchange(createURLWithPort("/users"), HttpMethod.POST, request,
        Object.class);

        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(400);
    }

    @Test
    void shouldUpdateUserSuccessFully() throws CustomBadRequestException, CustomNotFoundException {

        HttpEntity<UserDTO> request = new HttpEntity<UserDTO>(userDTO, headers);

        ResponseEntity<Object> actual = restTemplate.exchange(createURLWithPort("/users/1"), HttpMethod.PUT, request,
        Object.class);

        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(200);
    }

    @Test
    void badIdsShouldReturnException() throws CustomBadRequestException, CustomNotFoundException {

        HttpEntity<UserDTO> request = new HttpEntity<UserDTO>(userDTO, headers);

        ResponseEntity<Object> actual = restTemplate.exchange(createURLWithPort("/users/2"), HttpMethod.PUT, request,
        Object.class);

        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(400);
    }

    @Test
    void nonExistingIdShouldReturnException() throws CustomBadRequestException, CustomNotFoundException {

        userDTO.setUserId(2L);

        HttpEntity<UserDTO> request = new HttpEntity<UserDTO>(userDTO, headers);

        ResponseEntity<Object> actual = restTemplate.exchange(createURLWithPort("/users/2"), HttpMethod.PUT, request,
        Object.class);

        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(404);
    }

    @Test
    void shouldDeleteUserSuccessFully() throws CustomNotFoundException {

        userDTO.setUserId(null);

        HttpEntity<UserDTO> addRequest = new HttpEntity<UserDTO>(userDTO, headers);

        ResponseEntity<Object> addResponse = restTemplate.exchange(createURLWithPort("/users"), HttpMethod.POST, addRequest,
        Object.class);

        then(addResponse).as("Check if we have a response").isNotNull();
        then(addResponse.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(201);

        HttpEntity<String> request = new HttpEntity<String>(null, headers);

        ResponseEntity<Object> actual = restTemplate.exchange(createURLWithPort("/users/2"), HttpMethod.DELETE, request,
        Object.class);

        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(200);
    }

    @Test
    void nonExistingIdToDeleteShouldReturnException() throws CustomNotFoundException{

        HttpEntity<String> request = new HttpEntity<String>(null, headers);

        ResponseEntity<Object> actual = restTemplate.exchange(createURLWithPort("/users/4"), HttpMethod.DELETE, request,
        Object.class);

        then(actual).as("Check if we have a response").isNotNull();
        then(actual.getStatusCodeValue()).as("Check if we have the expected status").isEqualTo(404);
    }

    private static String obtainAccessToken() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("client_id", "qdHU01Wjp6kPULvBMjDW5b1cd0hgkpU7");
        requestBody.put("client_secret", "K3Zl2XuMsMTnwX_2Cm6EWON0Fwa5M36jGqsixfxw9x9AyQWkE1xNpRxXNtuMXDGb");
        requestBody.put("audience", "https://fiera.testapi.com");
        requestBody.put("grant_type", "client_credentials");

        HttpEntity<String> request = new HttpEntity<String>(requestBody.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject("https://dev-1g0mmdx5.auth0.com/oauth/token", request, String.class);

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(result).get("access_token").toString();
    }

    private String createURLWithPort(String url) {
        return "http://localhost:" + port + url;
    }

}