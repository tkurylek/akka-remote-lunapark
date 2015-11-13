package pl.kurylek.lunapark.news.report;

import akka.actor.ActorRef;
import akka.testkit.TestProbe;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import pl.kurylek.lunapark.news.report.event.BrokenPipeNewsEvent;
import pl.kurylek.lunapark.news.report.event.LooseCartNewsEvent;
import pl.kurylek.lunapark.news.report.event.NewsEvent;
import pl.kurylek.lunapark.news.report.event.NewsEventsProducingService;
import pl.kurylek.lunapark.news.report.event.TornadoAlertNewsEvent;
import pl.kurylek.lunapark.test.AkkaTestBase;

import static junitparams.JUnitParamsRunner.$;
import static org.mockito.BDDMockito.given;

@RunWith(JUnitParamsRunner.class)
public class NewsReporterTest extends AkkaTestBase {

	@Mock
	NewsEventsProducingService mockedNewsEventsProducingService;

	ActorRef newsReporter = system.actorOf(NewsReporter.props(mockedNewsEventsProducingService));

	@Test
	@Parameters
	public void shouldWarnAboutAnIncomingDanger(NewsEvent action, Class<? extends NewsReporter.NewsTopicMsg> expectedReaction) {
		// given
		TestProbe manager = new TestProbe(system);
		given(mockedNewsEventsProducingService.getNewsEvents()).willReturn(action);

		// when
		newsReporter.tell(new NewsReporter.NewsAirTimeMsg(), manager.ref());

		// then
		manager.expectMsgClass(expectedReaction);
	}

	public Object parametersForShouldWarnAboutAnIncomingDanger() {
		return $(
				$(new TornadoAlertNewsEvent(), NewsReporter.IncomingStormMsg.class),
				$(new LooseCartNewsEvent(), NewsReporter.RollercoasterFearGrowthMsg.class),
				$(new BrokenPipeNewsEvent(), NewsReporter.RollercoasterFearGrowthMsg.class)
		);
	}
}