package com.mitigant.simpletodobackend.controllers;

import com.mitigant.simpletodobackend.dto.TodoTaskItemDto;
import com.mitigant.simpletodobackend.model.TodoTaskItems;
import com.mitigant.simpletodobackend.services.TodoTaskItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{id}/description")
    public ResponseEntity updateDescription(@PathVariable Long id, @RequestBody TodoTaskItemDto todoTaskItemDto) {
        try{
            todoTaskItemService.updateDescription(id,todoTaskItemDto);

        }
        catch (Exception e){
            return new ResponseEntity("Error Updating the Status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("Description updated Successfully!!!", HttpStatus.OK);
        //return service.updateDescription(id, description);
    }

}
