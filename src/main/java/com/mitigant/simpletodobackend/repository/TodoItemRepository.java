package com.mitigant.simpletodobackend.repository;

import com.mitigant.simpletodobackend.enums.TodoItemStatus;
import com.mitigant.simpletodobackend.model.TodoTaskItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoItemRepository extends JpaRepository<TodoTaskItems,Long> {

    List<TodoTaskItems> findAllByStatus(TodoItemStatus status);
}
