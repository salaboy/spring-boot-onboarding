package io.diagrid.workflows;

import io.diagrid.workflows.services.CounterService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = {TestWorkflowsApplication.class, DaprTestContainersConfig.class},
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WorkflowsApplicationTests {

  @Autowired
  private CounterService counterService;
  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + 8080;
    org.testcontainers.Testcontainers.exposeHostPorts(8080);

  }


  @Test
	void testSimpleWorkflow () {

    int counterBeforeWorkflow = counterService.getCounter();
    String instanceId = RestAssured.given()
        .when()
        .post("/start")
        .then()
        .statusCode(200).extract().asString();

    assertNotNull(instanceId);

    // Check that the workflow completed successfully
    await().atMost(Duration.ofSeconds(2))
        .pollDelay(500, TimeUnit.MILLISECONDS)
        .pollInterval(500, TimeUnit.MILLISECONDS)
        .until(() -> {
          return counterService.getCounter() > counterBeforeWorkflow;
        });

	}

}
