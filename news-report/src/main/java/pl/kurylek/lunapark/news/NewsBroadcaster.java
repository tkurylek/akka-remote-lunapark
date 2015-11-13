package pl.kurylek.lunapark.news;

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
import pl.kurylek.lunapark.news.di.NewsActorFactory;
import pl.kurylek.lunapark.news.report.NewsReporter;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component("newsBroadcaster")
@Scope("prototype")
public class NewsBroadcaster extends AbstractLoggingActor {

	private static final String REPORT_DURATION = "news-broadcaster.reporter.air-time-duration";

	private final ActorRef reporter;
	private final ActorRef sensibleUnits;
	private final Scheduler scheduler;
	private final ActorSystem system;
	private final FiniteDuration reportInterval;

	@Autowired
	private NewsBroadcaster(NewsActorFactory actorFactory) {
		this.system = context().system();
		this.reporter = actorFactory.createNewsReportingActor(context());
		this.sensibleUnits = actorFactory.createNewsSensibleUnits(context());
		this.scheduler = actorFactory.scheduler(system);
		this.reportInterval = Duration.create(context().system()
				.settings().config().getDuration(REPORT_DURATION, MILLISECONDS), MILLISECONDS);

		receive(ReceiveBuilder
				.match(NewsReporter.NewsTopicMsg.class, this::handleMessage)
				.build());
	}

	public static Props props(NewsActorFactory actorFactory) {
		return Props.create(NewsBroadcaster.class, () -> new NewsBroadcaster(actorFactory));
	}

	@Override
	public void preStart() {
		reporter.tell(new NewsReporter.NewsAirTimeMsg(), self());
	}

	private void handleMessage(NewsReporter.NewsTopicMsg newsTopicMsg) {
		log().info("Received report - {} ", newsTopicMsg);
		scheduleReportIn(reportInterval);
		sensibleUnits.forward(newsTopicMsg, context());
	}

	private void scheduleReportIn(FiniteDuration finiteDuration) {
		log().debug("Another report has been scheduled in {} ", finiteDuration);
		scheduler.scheduleOnce(finiteDuration, () -> {
			reporter.tell(new NewsReporter.NewsAirTimeMsg(), self());
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
		sender().tell(new NewsReporter.NewsAirTimeMsg(), self());
		return SupervisorStrategy.restart();
	}
}
