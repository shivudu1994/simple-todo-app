package com.mitigant.simpletodobackend.model;

import com.mitigant.simpletodobackend.enums.TodoItemStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mitigant_todo_app")
public class TodoTaskItems {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false)
    private String description;

    private TodoItemStatus status;
    @CreationTimestamp
    private LocalDateTime createdDateTime;

    private LocalDateTime dueDateTime;
    @CreationTimestamp
    private LocalDateTime doneDateTime;

}
