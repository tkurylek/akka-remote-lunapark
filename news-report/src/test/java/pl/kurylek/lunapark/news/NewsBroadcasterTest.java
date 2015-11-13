package pl.kurylek.lunapark.news;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import akka.testkit.TestProbe;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import pl.kurylek.lunapark.news.di.NewsActorFactory;
import pl.kurylek.lunapark.news.report.NewsReporter;
import pl.kurylek.lunapark.news.report.event.NewsEventsProducingService;
import pl.kurylek.lunapark.test.AkkaTestBase;
import scala.concurrent.duration.FiniteDuration;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static junitparams.JUnitParamsRunner.$;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class NewsBroadcasterTest extends AkkaTestBase {

	@Mock
	NewsEventsProducingService mockedNewsEventsProducingService;

	@Mock
	Scheduler mockedScheduler;

	ActorRef newsBroadcaster;

	@Test
	@Parameters
	public void shouldBroadcastNews(NewsReporter.NewsTopicMsg newsTopic) {
		// given
		TestProbe newsReporter = new TestProbe(system);
		TestProbe rollercoasterGatekeeper = new TestProbe(system);
		newsBroadcaster = createNewsBroadcasterWithMockedDependencies(newsReporter.ref(), rollercoasterGatekeeper.ref());

		// when
		newsBroadcaster.tell(newsTopic, newsReporter.ref());

		// then
		rollercoasterGatekeeper.expectMsg(newsTopic);
		verify(mockedScheduler).scheduleOnce(eq(FiniteDuration.create(10, MILLISECONDS)), any(Runnable.class), eq(system.dispatcher()));
		newsReporter.expectMsgClass(NewsReporter.NewsAirTimeMsg.class);
	}

	public Object parametersForShouldBroadcastNews() {
		return $(
				new NewsReporter.IncomingStormMsg(),
				new NewsReporter.RollercoasterFearGrowthMsg()
		);
	}

	private ActorRef createNewsBroadcasterWithMockedDependencies(ActorRef reporter, ActorRef sensibleUnit) {
		return system.actorOf(NewsBroadcaster.props(new NewsActorFactory(mockedNewsEventsProducingService) {
			@Override
			public ActorRef createNewsReportingActor(ActorContext context) {
				return reporter;
			}

			@Override
			public ActorRef createNewsSensibleUnits(ActorContext context) {
				return sensibleUnit;
			}

			@Override
			public Scheduler scheduler(ActorSystem system) {
				return mockedScheduler;
			}
		}), "news-broadcaster");
	}
}