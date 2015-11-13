package pl.kurylek.lunapark.employees;

import akka.actor.ActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kurylek.lunapark.common.SpringExtension;

@Configuration
public class ApplicationConfiguration {
	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public ActorSystem actorSystem() {
		ActorSystem system = ActorSystem.create("employees");
		SpringExtension.SpringExtProvider.get(system).initialize(applicationContext);
		return system;
	}
}
