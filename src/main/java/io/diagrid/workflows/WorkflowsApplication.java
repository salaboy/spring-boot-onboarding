package io.diagrid.workflows;

import io.dapr.spring.workflows.config.EnableDaprWorkflows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDaprWorkflows
public class WorkflowsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkflowsApplication.class, args);
	}

}
