package pl.kurylek.lunapark.employees.kiosk;

import akka.actor.AbstractLoggingActor;
import akka.japi.pf.ReceiveBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.kurylek.lunapark.weather.report.WeatherReporter;

@Component("kioskSalesman")
@Scope("prototype")
public class KioskSalesman extends AbstractLoggingActor {

	private KioskSalesman() {
		receive(ReceiveBuilder
				.match(WeatherReporter.SunnyWeatherMsg.class, this::handleMessage)
				.match(WeatherReporter.RainyWeatherMsg.class, this::handleMessage)
				.match(WeatherReporter.FoggyWeatherMsg.class, this::handleMessage)
				.build());
	}

	private void handleMessage(WeatherReporter.SunnyWeatherMsg message) {
		log().info("Umbrellas are sold at normal price.");
	}

	private void handleMessage(WeatherReporter.RainyWeatherMsg message) {
		log().info("Umbrellas cost twice as before.");
	}

	private void handleMessage(WeatherReporter.FoggyWeatherMsg message) {
		throw new IllegalStateException("In this foggy weather the salesman has been kidnapped!");
	}
}
