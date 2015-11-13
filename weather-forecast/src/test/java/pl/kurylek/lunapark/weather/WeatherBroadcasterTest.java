package pl.kurylek.lunapark.weather;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import akka.testkit.TestProbe;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import pl.kurylek.lunapark.weather.di.ForecastActorFactory;
import pl.kurylek.lunapark.weather.report.event.WeatherEventsProducingService;
import pl.kurylek.lunapark.test.AkkaTestBase;
import pl.kurylek.lunapark.weather.report.WeatherReporter;
import scala.concurrent.duration.FiniteDuration;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static junitparams.JUnitParamsRunner.$;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class WeatherBroadcasterTest extends AkkaTestBase {

	@Mock
	WeatherEventsProducingService mockedWeatherEventsProducingService;

	@Mock
	Scheduler mockedScheduler;

	ActorRef weatherBroadcaster;

	@Test
	@Parameters
	public void shouldBroadcastWeather(WeatherReporter.WeatherMsg weatherMsg) {
		// given
		TestProbe weatherReporter = new TestProbe(system);
		TestProbe rollercoasterGatekeeper = new TestProbe(system);
		weatherBroadcaster = createWeatherBroadcasterWithMockedDependencies(weatherReporter.ref(), rollercoasterGatekeeper.ref());

		// when
		weatherBroadcaster.tell(weatherMsg, weatherReporter.ref());

		// then
		rollercoasterGatekeeper.expectMsg(weatherMsg);
		verify(mockedScheduler).scheduleOnce(eq(FiniteDuration.create(10, MILLISECONDS)), any(Runnable.class), Matchers.eq(system.dispatcher()));
		weatherReporter.expectMsgClass(WeatherReporter.WeatherReportAirTime.class);
	}

	public Object parametersForShouldBroadcastWeather() {
		return $(
				new WeatherReporter.SunnyWeatherMsg(),
				new WeatherReporter.RainyWeatherMsg(),
				new WeatherReporter.FoggyWeatherMsg()
		);
	}

	private ActorRef createWeatherBroadcasterWithMockedDependencies(ActorRef reporter, ActorRef sensibleUnit) {
		return system.actorOf(WeatherBroadcaster.props(new ForecastActorFactory(mockedWeatherEventsProducingService) {

			@Override
			public ActorRef createWeatherReportingActor(ActorContext context) {
				return reporter;
			}

			@Override
			public ActorRef createWeatherSensibleUnits(ActorContext context) {
				return sensibleUnit;
			}

			@Override
			public Scheduler scheduler(ActorSystem system) {
				return mockedScheduler;
			}
		}), "weather-broadcaster");
	}
}