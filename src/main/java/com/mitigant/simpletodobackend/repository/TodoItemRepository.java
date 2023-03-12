package com.mitigant.simpletodobackend.repository;

import com.mitigant.simpletodobackend.model.TodoTaskItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoItemRepository extends JpaRepository<TodoTaskItems,Long> {
}
