package pl.kurylek.lunapark.employees.rollercoaster;

import akka.actor.AbstractLoggingActor;
import akka.japi.pf.ReceiveBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.kurylek.lunapark.news.report.NewsReporter;
import pl.kurylek.lunapark.weather.report.WeatherReporter;

@Component("rollercoasterGatekeeper")
@Scope("prototype")
public class RollercoasterGatekeeper extends AbstractLoggingActor {

	public RollercoasterGatekeeper() {
		receive(ReceiveBuilder
				.match(WeatherReporter.SunnyWeatherMsg.class, this::handleMessage)
				.match(WeatherReporter.RainyWeatherMsg.class, this::handleMessage)
				.match(WeatherReporter.FoggyWeatherMsg.class, this::handleMessage)
				.match(NewsReporter.RollercoasterFearGrowthMsg.class, this::handleMessage)
				.match(NewsReporter.IncomingStormMsg.class, this::handleMessage)
				.build());
	}

	private void handleMessage(WeatherReporter.SunnyWeatherMsg message) {
		log().info("Rollercoaster is open!");
	}

	private void handleMessage(WeatherReporter.RainyWeatherMsg message) {
		log().info("Rollercoaster is closed due to bad weather!");
	}

	private void handleMessage(WeatherReporter.FoggyWeatherMsg message) {
		log().info("Rollercoaster is closed due to bad weather!");
	}

	private void handleMessage(NewsReporter.RollercoasterFearGrowthMsg message) {
		log().info("Rollercoaster ride cost is lower!");
	}

	private void handleMessage(NewsReporter.IncomingStormMsg message) {
		log().info("Rollercoaster is closed due to incoming storm!");
	}
}
