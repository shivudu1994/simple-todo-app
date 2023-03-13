package com.mitigant.simpletodobackend.repository;

import com.mitigant.simpletodobackend.enums.TodoItemStatus;
import com.mitigant.simpletodobackend.model.TodoTaskItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoItemRepository extends JpaRepository<TodoTaskItems,Long> {

    List<TodoTaskItems> findAllByStatus(TodoItemStatus status);

    List<TodoTaskItems> findByDueDateTimeBeforeAndStatusNot(LocalDateTime now, TodoItemStatus pastDue);
}
