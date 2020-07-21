package com.fiera.api.Controller;

import java.util.Arrays;
import java.util.List;

import com.fiera.api.controllers.UserController;
import com.fiera.api.dtos.UserDTO;
import com.fiera.api.services.UserService;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.client.RestTemplate;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureTestDatabase
public class UserControllerIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void shouldReturnAllUsers() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setDocNumber("123456789");
        userDTO.setFirstName("Luke");
        userDTO.setLastName("Skywalker");
        userDTO.setEmail("jedi@email.com");
        userDTO.setPhone("987654321");
        userDTO.setAddress("123 death star avenue");

        List<UserDTO> users = Arrays.asList(userDTO);

        given(userService.getUsers()).willReturn(users);

        String accessToken = obtainAccessToken();

        mvc.perform(
                get("/users")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].firstName").value(userDTO.getFirstName()));
    }

    private String obtainAccessToken() throws Exception {
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

}