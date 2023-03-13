package com.mitigant.simpletodobackend;


import com.mitigant.simpletodobackend.dto.TodoTaskItemDto;
import com.mitigant.simpletodobackend.model.TodoTaskItems;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoItemControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Test
    public void testPostCreateTodoItemValid() throws URISyntaxException {
        final String baseUrl = "http://localhost:"+port+"/v1/api/todo-items/create-todo-item";
        URI uri = new URI(baseUrl);

        RestTemplate restTemplate = new RestTemplate();
        String requestJson = "{\n" +
                "  \"description\": \"shiva\",\n" +
                "  \"dueDateTime\": \"2023-03-20T10:19\"\n" +
                "}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> toDoItemEntity = new HttpEntity<String>(requestJson,headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri, toDoItemEntity, String.class);
        Assertions.assertEquals(200, result.getStatusCodeValue());

    }

    @Test
    public void testPostCreateTodoItemInvalidDescription() throws URISyntaxException {
        final String baseUrl = "http://localhost:"+port+"/v1/api/todo-items/create-todo-item";
        URI uri = new URI(baseUrl);

        RestTemplate restTemplate = new RestTemplate();
        String requestJson = "{\n" +
                "  \"description\": \"\",\n" +
                "  \"dueDateTime\": \"2023-03-11T10:19\"\n" + // update date to a future date
                "}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> toDoItemEntity = new HttpEntity<String>(requestJson,headers);

        try {
            ResponseEntity<String> result = restTemplate.postForEntity(uri, toDoItemEntity, String.class);
            Assertions.assertEquals("Description should not be empty!!!", result.getBody());
        } catch (HttpClientErrorException e) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assertions.assertEquals("Description should not be empty!!!", e.getResponseBodyAsString());
        }
    }
    @Test
    public void testPostCreateTodoItemInvalidDate() throws URISyntaxException {
        final String baseUrl = "http://localhost:"+port+"/v1/api/todo-items/create-todo-item";
        URI uri = new URI(baseUrl);

        RestTemplate restTemplate = new RestTemplate();
        String requestJson = "{\n" +
                "  \"description\": \" HI Task 2\",\n" +
                "  \"dueDateTime\": \"\"\n" + // update date to a future date
                "}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> toDoItemEntity = new HttpEntity<String>(requestJson,headers);

        try {
            ResponseEntity<String> result = restTemplate.postForEntity(uri, toDoItemEntity, String.class);
            Assertions.assertEquals("Due date time should not be empty!!!", result.getBody());
        } catch (HttpClientErrorException e) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            Assertions.assertEquals("Due date time should not be empty!!!", e.getResponseBodyAsString());
        }
    }

    @Test
    public void testUpdateDescription() {
        TodoTaskItemDto todoTaskItemDto = new TodoTaskItemDto();
        todoTaskItemDto.setDescription("New Description");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TodoTaskItemDto> requestEntity = new HttpEntity<>(todoTaskItemDto, headers);
        ResponseEntity<String> responseEntity = testRestTemplate.exchange("/v1/api/todo-items/1/description", HttpMethod.PUT, requestEntity, String.class);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals("Description updated Successfully!!!", responseEntity.getBody());
    }
    @Test
    public void testUpdateItemStatusAsDone() {
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/api/todo-items/update-done-status/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                1L
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("successfully set status to DONE!!!", response.getBody());
    }

    @Test
    public void testUpdateItemStatusAsDoneWithInvalidId() {
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/api/todo-items/update-done-status/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                "999L"
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }
    @Test
    public void testUpdateItemStatusAsNotDone() {
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/api/todo-items/update-not-done-status/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                1L
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("successfully set status to NOT DONE!!!", response.getBody());
    }

    @Test
    public void testUpdateItemStatusAsNotDoneWithInvalidId() {
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/v1/api/todo-items/update-not-done-status/{id}",
                HttpMethod.PUT,
                null,
                String.class,
                "999L"
        );
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testGetAllNotDone() {

        List<TodoTaskItems> toDoItemList = new ArrayList<>();
        toDoItemList.add(new TodoTaskItems());
        toDoItemList.add(new TodoTaskItems());
        toDoItemList.add(new TodoTaskItems());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<TodoTaskItems>> response = testRestTemplate.exchange(
                "/v1/api/todo-items/get-all-not-done",
                HttpMethod.GET, entity, new ParameterizedTypeReference<List<TodoTaskItems>>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(toDoItemList.size(), response.getBody().size());
    }

    @Test
    public void testGetAllById() {

        Long id = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<TodoTaskItems> response = testRestTemplate.exchange(
                "/v1/api/todo-items/get-by-id/{id}",
                HttpMethod.GET, entity, TodoTaskItems.class, id);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(id, response.getBody().getId());
    }


}

