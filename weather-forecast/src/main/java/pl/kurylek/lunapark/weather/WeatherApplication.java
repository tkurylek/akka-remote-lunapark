package pl.kurylek.lunapark.weather;

import akka.actor.ActorSystem;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.kurylek.lunapark.common.SpringContextProducer;

import static pl.kurylek.lunapark.common.SpringExtension.SpringExtProvider;

public class WeatherApplication {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = SpringContextProducer.produce("pl.kurylek.lunapark.weather");

		ActorSystem system = ctx.getBean(ActorSystem.class);
		system.actorOf(SpringExtProvider.get(system).props("weatherBroadcaster"), "weather-broadcaster");
	}
}
