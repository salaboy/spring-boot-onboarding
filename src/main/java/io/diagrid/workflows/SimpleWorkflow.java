package io.diagrid.workflows;

import io.dapr.workflows.Workflow;
import io.dapr.workflows.WorkflowStub;
import io.diagrid.workflows.activities.FirstActivity;
import org.springframework.stereotype.Component;

@Component
public class SimpleWorkflow implements Workflow {
  @Override
  public WorkflowStub create() {
    return ctx -> {
      String instanceId = ctx.getInstanceId();

      ctx.callActivity(FirstActivity.class.getName()).await();

      ctx.complete("Workflow completed");
    };
  }
}
