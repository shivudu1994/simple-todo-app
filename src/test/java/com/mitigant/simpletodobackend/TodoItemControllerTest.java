package com.mitigant.simpletodobackend;


import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoItemControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Test
    public void testPostCreateTodoItem() throws URISyntaxException {
        final String baseUrl = "http://localhost:"+port+"/v1/api/todo-items/create-item";
        URI uri = new URI(baseUrl);

        RestTemplate restTemplate = new RestTemplate();
        String requestJson = "{\n" +
                "  \"description\": \"shiva\",\n" +
                "  \"dueDateTime\": \"2023-03-12T10:19\"\n" +
                "}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> toDoItemEntity = new HttpEntity<String>(requestJson,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri, toDoItemEntity, String.class);
        Assertions.assertEquals(200, result.getStatusCodeValue());

    }

    @Test
    public void testUpdateItemStatusAsDone() {
        // Send a PUT request to the endpoint
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/api/todo-items/updateDoneStatus/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                1L
        );

        // Assert that the response is successful and contains the expected message
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("successfully set status to DONE!!!", response.getBody());
    }

    @Test
    public void testUpdateItemStatusAsDoneWithInvalidId() {
        // Send a PUT request to the endpoint with an invalid ID
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/api/todo-items/updateDoneStatus/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                "999L"
        );

        // Assert that the response is unsuccessful and contains the expected error message
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }
}
