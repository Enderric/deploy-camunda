package ctrlops.cam.bpm;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.management.Metrics;
import org.camunda.bpm.engine.management.MetricsQuery;
import org.camunda.bpm.engine.runtime.JobQuery;
import org.camunda.bpm.engine.query.Query;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CamundaMonitoringMetrics {

  private static final String NUMBER_OF_EXCLUSIVE_JOBS = "Number of exclusive jobs";
  private static final String NUMBER_OF_ACQUISITION_CYCLES = "Number of acquisition cycles";
  private static final String NUMBER_OF_JOBS = "Number of jobs";

  private final ManagementService service;

  public CamundaMonitoringMetrics(ProcessEngine engine) {
    super();
    Objects.requireNonNull(engine);
    this.service = engine.getManagementService();
  }

  @Bean
  public Gauge jobExecutionsSuccessful(MeterRegistry registry) {
    MetricsQuery query = service.createMetricsQuery().name(Metrics.JOB_SUCCESSFUL);
    return Gauge.builder("job.executions.successful", query::sum)
        .description("Successful job executions")
        .baseUnit(NUMBER_OF_JOBS)
        .register(registry);
  }

  @Bean
  public Gauge jobExecutionsFailed(MeterRegistry registry) {
    MetricsQuery query = service.createMetricsQuery().name(Metrics.JOB_FAILED);
    return Gauge.builder("job.executions.failed", query::sum)
        .description("Failed job executions")
        .baseUnit(NUMBER_OF_JOBS)
        .register(registry);
  }

  @Bean
  public Gauge jobExecutionsRejected(MeterRegistry registry) {
    MetricsQuery query = service.createMetricsQuery().name(Metrics.JOB_EXECUTION_REJECTED);
    return Gauge.builder("job.executions.rejected", query::sum)
        .description("Rejected jobs due to saturated execution resources")
        .baseUnit(NUMBER_OF_JOBS)
        .register(registry);
  }

  @Bean
  public Gauge jobAcquisitionsAttempted(MeterRegistry registry) {
    MetricsQuery query = service.createMetricsQuery().name(Metrics.JOB_ACQUISITION_ATTEMPT);
    return Gauge.builder("job.acquisitions.attempted", query::sum)
        .description("Performed job acquisition cycles")
        .baseUnit(NUMBER_OF_ACQUISITION_CYCLES)
        .register(registry);
  }

  @Bean
  public Gauge jobAcquisitionsSuccessful(MeterRegistry registry) {
    MetricsQuery query = service.createMetricsQuery().name(Metrics.JOB_ACQUIRED_SUCCESS);
    return Gauge.builder("job.acquisitions.successful", query::sum)
        .description("Successful job acquisitions")
        .baseUnit(NUMBER_OF_JOBS)
        .register(registry);
  }

  @Bean
  public Gauge jobAcquisitionsFailed(MeterRegistry registry) {
    MetricsQuery query = service.createMetricsQuery().name(Metrics.JOB_ACQUIRED_FAILURE);
    return Gauge.builder("job.acquisitions.failed", query::sum)
        .description("Failed job acquisitions")
        .baseUnit(NUMBER_OF_JOBS)
        .register(registry);
  }

  @Bean
  public Gauge jobLocksExclusive(MeterRegistry registry) {
    MetricsQuery query = service.createMetricsQuery().name(Metrics.JOB_LOCKED_EXCLUSIVE);
    return Gauge.builder("job.locks.exclusive", query::sum)
        .description("Exclusive jobs that are immediately locked and executed")
        .baseUnit(NUMBER_OF_EXCLUSIVE_JOBS)
        .register(registry);
  }

  @Bean
  public Gauge dueJobsInDB(MeterRegistry registry) {
    Query jobQuery = service.createJobQuery().executable().messages();
    return Gauge.builder("jobs.due", jobQuery::count)
        .description("Jobs from async continuation that are due")
        .register(registry);
  }

  // Additional metrics based on the org.camunda.bpm.engine.management.Metrics class
  // @Bean
  // public Gauge activityInstancesStarted(MeterRegistry registry) {
  //   MetricsQuery query = service.createMetricsQuery().name(Metrics.ACTIVITY_INSTANCE_START);
  //   return Gauge.builder("activity.instances.started", query::sum)
  //       .description("Activity instances started")
  //       .baseUnit("Number of activity instances")
  //       .register(registry);
  // }

  @Bean
  public Gauge executedDecisionInstances(MeterRegistry registry) {
    MetricsQuery query = service.createMetricsQuery().name(Metrics.EXECUTED_DECISION_INSTANCES);
    return Gauge.builder("decision.instances.executed", query::sum)
        .description("Executed decision instances")
        .baseUnit("Number of decision instances")
        .register(registry);
  }

  @Bean
  public Gauge executedDecisionElements(MeterRegistry registry) {
    MetricsQuery query = service.createMetricsQuery().name(Metrics.EXECUTED_DECISION_ELEMENTS);
    return Gauge.builder("decision.elements.executed", query::sum)
        .description("Executed decision elements")
        .baseUnit("Number of decision elements")
        .register(registry);
  }

  @Bean
  public Gauge rootProcessInstancesStarted(MeterRegistry registry) {
    MetricsQuery query = service.createMetricsQuery().name(Metrics.ROOT_PROCESS_INSTANCE_START);
    return Gauge.builder("root.process.instances.started", query::sum)
        .description("Root process instances started")
        .baseUnit("Number of process instances")
        .register(registry);
  }

  @Bean
  public Gauge taskMetrics(MeterRegistry registry) {
    MetricsQuery query = service.createMetricsQuery().name(Metrics.TASK_USERS);
    return Gauge.builder("task.user", query::sum)
        .description("Number of user tasks")
        .baseUnit("Number of tasks")
        .register(registry);
  }

  @Bean
  public Gauge processInstances(MeterRegistry registry) {
    MetricsQuery query = service.createMetricsQuery().name(Metrics.PROCESS_INSTANCES);
    return Gauge.builder("process.instances", query::sum)
        .description("Number of process instances")
        .baseUnit("Number of instaces")
        .register(registry);
  }
}