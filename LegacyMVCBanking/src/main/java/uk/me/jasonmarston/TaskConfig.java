package uk.me.jasonmarston;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class TaskConfig {
	@Bean
	public TaskExecutor getAsyncExecutor() {
		return new SimpleAsyncTaskExecutor();
	}

	@Bean
	public TaskScheduler taskScheduler() {
		final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(20);
		scheduler.setThreadNamePrefix("scheduler-");
		scheduler.setWaitForTasksToCompleteOnShutdown(true);
		return scheduler;
	}
}