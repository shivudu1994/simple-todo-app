package com.mitigant.simpletodobackend.controllers;

import com.mitigant.simpletodobackend.dto.TodoTaskItemDto;
import com.mitigant.simpletodobackend.model.TodoTaskItems;
import com.mitigant.simpletodobackend.services.TodoTaskItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/todo-items")
public class TodoItemController {

    @Autowired
    private final TodoTaskItemService todoTaskItemService;

    public TodoItemController(TodoTaskItemService todoTaskItemService) {
        this.todoTaskItemService = todoTaskItemService;
    }

    @PostMapping(path = "create-item")
    public TodoTaskItems createItems(@RequestBody TodoTaskItemDto todoTaskItemDto){
        return todoTaskItemService.create(todoTaskItemDto);
    }

}
