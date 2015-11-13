package pl.kurylek.lunapark.weather.report;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import lombok.Value;
import pl.kurylek.lunapark.weather.report.event.FoggyWeatherEvent;
import pl.kurylek.lunapark.weather.report.event.RainyWeatherEvent;
import pl.kurylek.lunapark.weather.report.event.SunnyWeatherEvent;
import pl.kurylek.lunapark.weather.report.event.WeatherEvent;
import pl.kurylek.lunapark.weather.report.event.WeatherEventsProducingService;

import java.io.Serializable;

public class WeatherReporter extends AbstractLoggingActor {

	private final WeatherEventsProducingService weatherEventsProducingService;

	public WeatherReporter(WeatherEventsProducingService weatherEventsProducingService) {
		this.weatherEventsProducingService = weatherEventsProducingService;
		receive(ReceiveBuilder
				.match(WeatherReportAirTime.class, this::handleMessage)
				.build());
	}

	public static Props props(WeatherEventsProducingService weatherEventsProducingService) {
		return Props.create(WeatherReporter.class, () -> new WeatherReporter(weatherEventsProducingService));
	}

	private void handleMessage(WeatherReportAirTime message) {
		WeatherEvent weatherEvent = weatherEventsProducingService.getWeatherEvent();
		log().debug("Sending forecast report");
		weatherEvent.accept(new WeatherEvent.Visitor() {
			@Override
			public void visit(SunnyWeatherEvent event) {
				sender().tell(new SunnyWeatherMsg(), self());
			}

			@Override
			public void visit(RainyWeatherEvent event) {
				sender().tell(new RainyWeatherMsg(), self());
			}

			@Override
			public void visit(FoggyWeatherEvent event) {
				sender().tell(new FoggyWeatherMsg(), self());
			}
		});
	}

	public interface WeatherMsg extends Serializable {
	}

	@Value
	public static class RainyWeatherMsg implements WeatherMsg {
	}

	@Value
	public static class SunnyWeatherMsg implements WeatherMsg {
	}

	@Value
	public static class FoggyWeatherMsg implements WeatherMsg {
	}

	@Value
	public static class WeatherReportAirTime implements Serializable {
	}
}
