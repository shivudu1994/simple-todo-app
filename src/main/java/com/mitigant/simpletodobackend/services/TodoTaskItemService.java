package com.mitigant.simpletodobackend.services;

import com.mitigant.simpletodobackend.dto.TodoTaskItemDto;
import com.mitigant.simpletodobackend.exceptions.TodoItemNotFoundException;
import com.mitigant.simpletodobackend.model.TodoTaskItems;
import com.mitigant.simpletodobackend.repository.TodoItemRepository;
import com.mitigant.simpletodobackend.enums.TodoItemStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
@Component
public class TodoTaskItemService {

    private final TodoItemRepository todoItemRepository;
    private static final Logger logger = LoggerFactory.getLogger(TodoTaskItemService.class);


    public TodoTaskItemService(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    public TodoTaskItems create(TodoTaskItemDto todoTaskItemDto) {
        TodoTaskItems todoTaskItems = new TodoTaskItems();
        todoTaskItems.setDescription(todoTaskItemDto.description);
        todoTaskItems.setStatus(TodoItemStatus.NOT_DONE);
        todoTaskItems.setDueDateTime(LocalDateTime.parse(todoTaskItemDto.dueDateTime));
        return todoItemRepository.save(todoTaskItems);
    }

    public TodoTaskItems updateDescription(Long id, TodoTaskItemDto todoTaskItemDto) {
        TodoTaskItems todoItem;
        try {
            todoItem = todoItemRepository.findById(id).orElseThrow(() -> new TodoItemNotFoundException(id));
        } catch (TodoItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(todoTaskItemDto.description != null)
            todoItem.setDescription(todoTaskItemDto.description);
        else{
            logger.error("Description should not be empty");
        }

        if(todoTaskItemDto.dueDateTime != null)
            todoItem.setDueDateTime(LocalDateTime.parse(todoTaskItemDto.dueDateTime));

        return todoItemRepository.save(todoItem);
    }
}
