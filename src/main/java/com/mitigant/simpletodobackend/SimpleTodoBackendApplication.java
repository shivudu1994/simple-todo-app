package com.mitigant.simpletodobackend;

import com.mitigant.simpletodobackend.model.TodoTaskItems;
import com.mitigant.simpletodobackend.enums.TodoItemStatus;
import com.mitigant.simpletodobackend.repository.TodoItemRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.time.LocalDateTime;


@SpringBootApplication
@EnableScheduling
public class SimpleTodoBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(SimpleTodoBackendApplication.class, args);
	}

	@Autowired
	private TodoItemRepository todoItemRepository;

	@Bean
	InitializingBean insertNewUser(){
		return () -> {
			LocalDateTime datetime = LocalDateTime.now();
			todoItemRepository.save(new TodoTaskItems(1,"Task1",TodoItemStatus.NOT_DONE, null,datetime.plusMinutes(2) ,null));
			todoItemRepository.save(new TodoTaskItems(2,"task2",TodoItemStatus.NOT_DONE, null,datetime.plusDays(2),null));
			todoItemRepository.save(new TodoTaskItems(3,"task3",TodoItemStatus.DONE, null,datetime.plusDays(2),null));
			todoItemRepository.save(new TodoTaskItems(4,"task4",TodoItemStatus.PAST_DUE, null,datetime.plusDays(2),null));

		};
	}

}
