package pl.kurylek.lunapark.weather.report;

import akka.actor.ActorRef;
import akka.testkit.TestProbe;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import pl.kurylek.lunapark.test.AkkaTestBase;
import pl.kurylek.lunapark.weather.report.event.FoggyWeatherEvent;
import pl.kurylek.lunapark.weather.report.event.RainyWeatherEvent;
import pl.kurylek.lunapark.weather.report.event.SunnyWeatherEvent;
import pl.kurylek.lunapark.weather.report.event.WeatherEvent;
import pl.kurylek.lunapark.weather.report.event.WeatherEventsProducingService;

import static junitparams.JUnitParamsRunner.$;
import static org.mockito.BDDMockito.given;

@RunWith(JUnitParamsRunner.class)
public class WeatherReporterTest extends AkkaTestBase {

	@Mock
	WeatherEventsProducingService mockedWeatherEventsProducingService;

	ActorRef weatherReporter = system.actorOf(WeatherReporter.props(mockedWeatherEventsProducingService));

	@Test
	@Parameters
	public void shouldForecastWeatherBasedOnObservations(WeatherEvent weatherEvent, Class<? extends WeatherReporter.WeatherMsg> expectedMessage) {
		// given
		TestProbe manager = new TestProbe(system);
		given(mockedWeatherEventsProducingService.getWeatherEvent()).willReturn(weatherEvent);

		// when
		weatherReporter.tell(new WeatherReporter.WeatherReportAirTime(), manager.ref());

		// then
		manager.expectMsgClass(expectedMessage);
	}

	public Object parametersForShouldForecastWeatherBasedOnObservations() {
		return $(
				$(new SunnyWeatherEvent(), WeatherReporter.SunnyWeatherMsg.class),
				$(new RainyWeatherEvent(), WeatherReporter.RainyWeatherMsg.class),
				$(new FoggyWeatherEvent(), WeatherReporter.FoggyWeatherMsg.class)
		);
	}
}