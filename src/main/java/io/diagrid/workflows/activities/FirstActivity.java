package io.diagrid.workflows.activities;

import io.dapr.workflows.WorkflowActivity;
import io.dapr.workflows.WorkflowActivityContext;
import io.diagrid.workflows.services.CounterService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FirstActivity implements WorkflowActivity {
  private Logger logger = org.slf4j.LoggerFactory.getLogger(FirstActivity.class);

  @Autowired
  private CounterService counterService;

  @Override
  public Object run(WorkflowActivityContext ctx) {
    logger.info("Executing the First activity.");
    counterService.incrementCounter();
    return null;
  }
}
