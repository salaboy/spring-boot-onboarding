package io.diagrid.workflows;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestWorkflowsApplication {

	public static void main(String[] args) {
    SpringApplication.from(WorkflowsApplication::main)
        .with(DaprTestContainersConfig.class)
        .run(args);
    org.testcontainers.Testcontainers.exposeHostPorts(8080);
	}

}
