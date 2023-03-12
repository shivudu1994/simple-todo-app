package com.mitigant.simpletodobackend.controllers;

import com.mitigant.simpletodobackend.dto.TodoTaskItemDto;
import com.mitigant.simpletodobackend.model.TodoTaskItems;
import com.mitigant.simpletodobackend.services.TodoTaskItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{id}/description")
    public ResponseEntity updateDescription(@PathVariable Long id, @RequestBody TodoTaskItemDto todoTaskItemDto) {
        try {
            if(!todoTaskItemDto.description.isEmpty()) {
                todoTaskItemService.updateDescription(id, todoTaskItemDto);
                return new ResponseEntity("Description updated Successfully!!!", HttpStatus.OK);
            } else {
                return new ResponseEntity("Description Should not be empty", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity("Error Updating the Status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-done-status/{id}")
    public ResponseEntity updateItemStatusAsDone(@PathVariable Long id) {
        try{
            todoTaskItemService.updateItemStatusAsDone(id);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity("successfully set status to DONE!!!", HttpStatus.OK);
    }

    @PutMapping("/update-not-done-status/{id}")
    public ResponseEntity updateStatusNotDone(@PathVariable Long id) {
        try{
            todoTaskItemService.updateStatusNotDone(id);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return new ResponseEntity("successfully set status to NOT DONE!!!", HttpStatus.OK);
    }

    @GetMapping("/get-all-not-done")
    public ResponseEntity getAllNotDone() {
        List<TodoTaskItems> toDoItemList = null;
        try{
            toDoItemList = todoTaskItemService.getAllNotdone();
        }
        catch (Exception e){
            return new ResponseEntity("fetching all todos with status NOT DONE was Erroneous",HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity(toDoItemList, HttpStatus.OK);

    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity getAllById(@PathVariable Long id){
        TodoTaskItems todoTaskItems = null;
        try{
            todoTaskItems = todoTaskItemService.findById(id);
        }
        catch (Exception e){
            return new ResponseEntity("fetching all todos with id: "+id +"was Erroneous",HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity(todoTaskItems, HttpStatus.OK);
    }

}
