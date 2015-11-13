package pl.kurylek.lunapark.weather;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.Scheduler;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.kurylek.lunapark.weather.di.ForecastActorFactory;
import pl.kurylek.lunapark.weather.report.WeatherReporter;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component("weatherBroadcaster")
@Scope("prototype")
public class WeatherBroadcaster extends AbstractLoggingActor {

	private static final String REPORT_DURATION = "weather-broadcaster.reporter.air-time-duration";

	private final ActorRef reporter;
	private final ActorRef sensibleUnits;
	private final Scheduler scheduler;
	private final ActorSystem system;
	private FiniteDuration reportInterval;

	@Autowired
	public WeatherBroadcaster(ForecastActorFactory actorFactory) {
		this.system = context().system();
		this.reporter = actorFactory.createWeatherReportingActor(context());
		this.sensibleUnits = actorFactory.createWeatherSensibleUnits(context());
		this.scheduler = actorFactory.scheduler(system);
		this.reportInterval = Duration.create(context().system()
				.settings().config().getDuration(REPORT_DURATION, MILLISECONDS), MILLISECONDS);

		receive(ReceiveBuilder
				.match(WeatherReporter.WeatherMsg.class, this::handleMessage)
				.build());
	}

	public static Props props(ForecastActorFactory actorFactory) {
		return Props.create(WeatherBroadcaster.class, () -> new WeatherBroadcaster(actorFactory));
	}

	@Override
	public void preStart() {
		reporter.tell(new WeatherReporter.WeatherReportAirTime(), self());
	}

	private void handleMessage(WeatherReporter.WeatherMsg weatherStatusMsg) {
		log().info("Received report - {} ", weatherStatusMsg);
		scheduleReportIn(reportInterval);
		sensibleUnits.forward(weatherStatusMsg, context());
	}

	private void scheduleReportIn(FiniteDuration finiteDuration) {
		log().debug("Another report has been scheduled in {} ", finiteDuration);
		scheduler.scheduleOnce(finiteDuration, () -> {
			reporter.tell(new WeatherReporter.WeatherReportAirTime(), self());
		}, system.dispatcher());
	}

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return new OneForOneStrategy(true, DeciderBuilder
				.matchAny(this::handleError)
				.build());
	}

	private SupervisorStrategy.Directive handleError(Object any) {
		log().error("Detected reporter failure [at:{}]", sender().path());
		sender().tell(new WeatherReporter.WeatherReportAirTime(), self());
		return SupervisorStrategy.restart();
	}
}
