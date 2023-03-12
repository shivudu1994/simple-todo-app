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

    @CreationTimestamp
    private LocalDateTime dueDateTime;
    @CreationTimestamp
    private LocalDateTime doneDateTime;

    public TodoTaskItems(){
        this.status = TodoItemStatus.valueOf("NOT_DONE");
    }
}
