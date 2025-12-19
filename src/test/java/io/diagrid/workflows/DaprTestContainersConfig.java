package io.diagrid.workflows;

import io.dapr.testcontainers.Component;
import io.dapr.testcontainers.DaprContainer;
import io.dapr.testcontainers.WorkflowDashboardContainer;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.Network;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TestConfiguration(proxyBeanMethods = false)
public class DaprTestContainersConfig {

  Map<String, String> postgreSQLDetails = new HashMap<>();

  {{
    postgreSQLDetails.put("host", "postgresql");
    postgreSQLDetails.put("user", "postgres");
    postgreSQLDetails.put("password", "postgres");
    postgreSQLDetails.put("database", "dapr");
    postgreSQLDetails.put("port", "5432");
    postgreSQLDetails.put("actorStateStore", String.valueOf(true));

  }}

  private Component stateStoreComponent = new Component("kvstore",
      "state.postgresql", "v2", postgreSQLDetails);

  @Bean
  @ServiceConnection
  DaprContainer daprContainer(Network network, PostgreSQLContainer postgreSQLContainer) {

    return new DaprContainer(DaprContainer.getDefaultImageName())
        .withNetwork(network)
        .withAppName("demo-dapr")
        .withAppPort(8080)
        .withComponent(stateStoreComponent)
        .withAppChannelAddress("host.testcontainers.internal")
        .withAppHealthCheckPath("/actuator/health")
        .dependsOn(postgreSQLContainer);
  }

  @Bean
  public WorkflowDashboardContainer workflowDashboard(Network network, PostgreSQLContainer postgreSQLContainer) {
    return new WorkflowDashboardContainer(WorkflowDashboardContainer.getDefaultImageName())
        .withNetwork(network)
        .withStateStoreComponent(stateStoreComponent)
        .withExposedPorts(8080)
        .dependsOn(postgreSQLContainer);
  }

  @Bean
  public PostgreSQLContainer postgreSQLContainer(Network network) {
    return new PostgreSQLContainer(DockerImageName.parse("postgres"))
        .withNetworkAliases("postgresql")
        .withDatabaseName("dapr")
        .withUsername("postgres")
        .withPassword("postgres")
        .withNetwork(network);
  }

  @Bean
  public Network getDaprNetwork(Environment env) {
    boolean reuse = env.getProperty("reuse", Boolean.class, false);
    if (reuse) {
      Network defaultDaprNetwork = new Network() {
        @Override
        public String getId() {
          return "dapr-network";
        }

        @Override
        public void close() {

        }

        @Override
        public Statement apply(Statement base, Description description) {
          return null;
        }
      };

      List<com.github.dockerjava.api.model.Network> networks = DockerClientFactory.instance().client().listNetworksCmd()
          .withNameFilter("dapr-network").exec();
      if (networks.isEmpty()) {
        Network.builder().createNetworkCmdModifier(cmd -> cmd.withName("dapr-network")).build().getId();
        return defaultDaprNetwork;
      } else {
        return defaultDaprNetwork;
      }
    } else {
      return Network.newNetwork();
    }
  }

}
