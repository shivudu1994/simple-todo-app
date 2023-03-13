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
import java.util.List;
import java.util.Optional;

@Service
@Component
public class TodoTaskItemService {

    private final TodoItemRepository todoItemRepository;
    private static final Logger logger = LoggerFactory.getLogger(TodoTaskItemService.class);


    public TodoTaskItemService(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }


    public TodoTaskItems createTodoTaskItem(TodoTaskItemDto todoTaskItemDto) {
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
        if (todoTaskItemDto.description != null) {
            todoItem.setDescription(todoTaskItemDto.description);
            logger.info("Description has been updated !! ");
        } else {
            logger.error("Description should not be empty");
        }

        return todoItemRepository.save(todoItem);

    }

    public void updateItemStatusAsDone(Long id) {
        try {
            todoItemRepository.findById(id).ifPresent(toDoItem -> {
                        if (toDoItem.getStatus() != TodoItemStatus.DONE) {
                            toDoItem.setStatus(TodoItemStatus.DONE);
                            toDoItem.setDoneDateTime(LocalDateTime.now());
                            todoItemRepository.save(toDoItem);
                            logger.info("Updated the Item status to done");

                        } else {
                            throw new RuntimeException("Cannot Update Todos with Status Past Due or Done");
                        }
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStatusNotDone(Long id) {
        try {
            todoItemRepository.findById(id).ifPresent(user -> {
                if (user.getStatus() != TodoItemStatus.PAST_DUE) {
                    user.setStatus(TodoItemStatus.NOT_DONE);
                    todoItemRepository.save(user);
                    logger.info("Updated the Item status to not done");
                } else {
                    throw new RuntimeException("Cannot Update Todos with Status Past Due");
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<TodoTaskItems> getAllNotdone() {
        try {
            logger.info("fetched all the item where the status is not done");
            return todoItemRepository.findAllByStatus(TodoItemStatus.NOT_DONE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<TodoTaskItems> getAllPastDue(Long id , TodoItemStatus status) {
        try {
            logger.info("fetched all the item where the status is not past due");
            return todoItemRepository.findByIdAndStatusNot(id , status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public TodoTaskItems findById(Long id) {
        Optional<TodoTaskItems> toDoItemoptional = todoItemRepository.findById(id);
        if (toDoItemoptional.isPresent()) {
            return toDoItemoptional.get();
        } else {
            throw new RuntimeException("Nothing Found");
        }

    }
}
