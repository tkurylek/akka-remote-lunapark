package pl.kurylek.lunapark.weather.di;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import akka.routing.FromConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kurylek.lunapark.weather.report.event.WeatherEventsProducingService;
import pl.kurylek.lunapark.weather.report.WeatherReporter;

@Service
public class ForecastActorFactory {

	private WeatherEventsProducingService weatherEventsProducingService;

	@Autowired
	public ForecastActorFactory(WeatherEventsProducingService weatherEventsProducingService) {
		this.weatherEventsProducingService = weatherEventsProducingService;
	}

	public ActorRef createWeatherReportingActor(ActorContext context) {
		return context.actorOf(WeatherReporter.props(weatherEventsProducingService), "weather-reporter");
	}

	public ActorRef createWeatherSensibleUnits(ActorContext context) {
		return context.actorOf(FromConfig.getInstance().props(), "weather-sensible");
	}

	public Scheduler scheduler(ActorSystem system) {
		return system.scheduler();
	}
}
