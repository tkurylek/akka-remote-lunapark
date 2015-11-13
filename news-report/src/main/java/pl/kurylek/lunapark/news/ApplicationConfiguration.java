package pl.kurylek.lunapark.news;

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
		ActorSystem system = ActorSystem.create("news");
		SpringExtension.SpringExtProvider.get(system).initialize(applicationContext);
		return system;
	}
}
