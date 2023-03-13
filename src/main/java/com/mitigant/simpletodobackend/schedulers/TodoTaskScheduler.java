package com.mitigant.simpletodobackend.schedulers;

import com.mitigant.simpletodobackend.enums.TodoItemStatus;
import com.mitigant.simpletodobackend.model.TodoTaskItems;
import com.mitigant.simpletodobackend.repository.TodoItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TodoTaskScheduler {

    private final TodoItemRepository todoItemRepository;
    private final Logger logger = LoggerFactory.getLogger(TodoTaskScheduler.class);

    @Autowired
    public TodoTaskScheduler(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void updateToPastDue() {
        logger.info("Cron job is running");
        List<TodoTaskItems> toDoItemList = todoItemRepository.findByDueDateTimeBeforeAndStatusNot(
                LocalDateTime.now(),
                TodoItemStatus.PAST_DUE
        );
        for (TodoTaskItems todoTaskItems: toDoItemList) {
            todoTaskItems.setStatus(TodoItemStatus.PAST_DUE);
            todoItemRepository.save(todoTaskItems);
            logger.info("ToDo Task Items are Updated");

        }
    }
}
