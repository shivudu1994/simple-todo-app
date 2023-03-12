package com.mitigant.simpletodobackend.controllers;

import com.mitigant.simpletodobackend.dto.TodoTaskItemDto;
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

    @PostMapping(path = "/create-item")
    public ResponseEntity createItems(@RequestBody TodoTaskItemDto todoTaskItemDto){
        return new ResponseEntity(todoTaskItemService.createTodoTaskItem(todoTaskItemDto), HttpStatus.OK);
    }

    @PatchMapping("/{id}/description")
    public ResponseEntity updateDescription(@PathVariable Long id, @RequestBody TodoTaskItemDto todoTaskItemDto) {
        try{
            if(!todoTaskItemDto.description.isEmpty()){
                todoTaskItemService.updateDescription(id,todoTaskItemDto);
            }
            else {
                return new ResponseEntity("Description Should not be empty", HttpStatus.BAD_REQUEST);
            }

        }
        catch (Exception e){
            return new ResponseEntity("Error Updating the Status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("Description updated Successfully!!!", HttpStatus.OK);
    }

    @PutMapping("/updateDoneStatus/{id}")
    public ResponseEntity updateItemStatusAsDone(@PathVariable Long id) {
        try{
            todoTaskItemService.updateItemStatusAsDone(id);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity("successfully set status to DONE!!!", HttpStatus.OK);
    }

}
