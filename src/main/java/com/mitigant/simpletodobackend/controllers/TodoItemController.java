package com.mitigant.simpletodobackend.controllers;

import com.mitigant.simpletodobackend.dto.TodoTaskItemDto;
import com.mitigant.simpletodobackend.enums.TodoItemStatus;
import com.mitigant.simpletodobackend.model.TodoTaskItems;
import com.mitigant.simpletodobackend.services.TodoTaskItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/v1/api/todo-items")
public class TodoItemController {

    @Autowired
    private final TodoTaskItemService todoTaskItemService;

    public TodoItemController(TodoTaskItemService todoTaskItemService) {
        this.todoTaskItemService = todoTaskItemService;
    }

    @PostMapping(path = "/create-todo-item")
    public ResponseEntity createItems(@RequestBody TodoTaskItemDto todoTaskItemDto){
        boolean isEmptyDescription = todoTaskItemDto.description.isEmpty();
        boolean isDueDateEmptyOrNull = todoTaskItemDto.dueDateTime == null || todoTaskItemDto.dueDateTime.isEmpty();

        if(isEmptyDescription) {
            return new ResponseEntity("Description should not be empty!!!", HttpStatus.BAD_REQUEST);
        }
        else if(isDueDateEmptyOrNull) {
            return new ResponseEntity("Due date time should not be empty!!!", HttpStatus.BAD_REQUEST);
        }
        else {
            try {
                LocalDateTime dueDateTime = LocalDateTime.parse(todoTaskItemDto.dueDateTime);
                if(dueDateTime.isBefore(LocalDateTime.now())) {
                    return new ResponseEntity("Due date should be in the future", HttpStatus.BAD_REQUEST);
                }
                else {
                    return new ResponseEntity(todoTaskItemService.createTodoTaskItem(todoTaskItemDto), HttpStatus.OK);
                }
            } catch(DateTimeParseException e) {
                return new ResponseEntity("Invalid date time format. Expected format: yyyy-MM-dd'T'HH:mm", HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PutMapping("/{id}/description")
    public ResponseEntity updateDescription(@PathVariable Long id, @RequestBody TodoTaskItemDto todoTaskItemDto) {
        try {
            if (!todoTaskItemDto.description.isEmpty()) {
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

        List<TodoTaskItems> toDoPastDueList = todoTaskItemService.getAllPastDue(id,TodoItemStatus.PAST_DUE);
        List<TodoTaskItems> toDoDoneList = todoTaskItemService.getAllPastDue(id,TodoItemStatus.PAST_DUE);
        try {
            boolean isAnyItemPastDue = toDoPastDueList.stream().anyMatch(item -> item.getId() == id && item.getStatus() == TodoItemStatus.PAST_DUE);
            boolean isAnyItemDone = toDoDoneList.stream().anyMatch(item -> item.getId() == id && item.getStatus() == TodoItemStatus.DONE);
            if (isAnyItemPastDue) {
                return new ResponseEntity("You can't update the status which is already PAST_DUE", HttpStatus.FORBIDDEN);
            } else if (isAnyItemDone) {
                return new ResponseEntity("You can't update the status which is already DONE", HttpStatus.FORBIDDEN);

            } else {
                todoTaskItemService.updateItemStatusAsDone(id);
                return new ResponseEntity("Item status updated successfully", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-not-done-status/{id}")
    public ResponseEntity updateStatusNotDone(@PathVariable Long id) {
        List<TodoTaskItems> toDoPastDueList = todoTaskItemService.getAllPastDue(id,TodoItemStatus.PAST_DUE);

        try {
            boolean isAnyItemPastDue = toDoPastDueList.stream().anyMatch(item -> item.getId() == id && item.getStatus() == TodoItemStatus.PAST_DUE);
            if(isAnyItemPastDue){
                return new ResponseEntity("You can't update the status which is already PAST_DUE", HttpStatus.FORBIDDEN);
            }
            else{
                todoTaskItemService.updateStatusNotDone(id);
                return new ResponseEntity("successfully set status to NOT DONE!!!", HttpStatus.OK);
            }

        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/get-all-not-done")
    public ResponseEntity getAllNotDone() {
        List<TodoTaskItems> toDoItemList = null;
        try {
            toDoItemList = todoTaskItemService.getAllNotdone();
        } catch (Exception e) {
            return new ResponseEntity("fetching all todos with status NOT DONE was Erroneous", HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity(toDoItemList, HttpStatus.OK);

    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity getAllById(@PathVariable Long id) {
        TodoTaskItems todoTaskItems = null;
        try {
            todoTaskItems = todoTaskItemService.findById(id);
        } catch (Exception e) {
            return new ResponseEntity("fetching all todos with id: " + id + "was Erroneous", HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity(todoTaskItems, HttpStatus.OK);
    }

}
