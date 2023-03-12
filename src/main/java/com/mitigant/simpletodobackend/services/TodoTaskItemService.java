package com.mitigant.simpletodobackend.services;

import com.mitigant.simpletodobackend.dto.TodoTaskItemDto;
import com.mitigant.simpletodobackend.model.TodoTaskItems;
import com.mitigant.simpletodobackend.repository.TodoItemRepository;
import com.mitigant.simpletodobackend.enums.TodoItemStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Component
public class TodoTaskItemService {

    private final TodoItemRepository todoItemRepository;


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
}
