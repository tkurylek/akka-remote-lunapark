package pl.kurylek.lunapark.news.report;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import lombok.Value;
import pl.kurylek.lunapark.news.report.event.BrokenPipeNewsEvent;
import pl.kurylek.lunapark.news.report.event.LooseCartNewsEvent;
import pl.kurylek.lunapark.news.report.event.NewsEvent;
import pl.kurylek.lunapark.news.report.event.NewsEventsProducingService;
import pl.kurylek.lunapark.news.report.event.TornadoAlertNewsEvent;

import java.io.Serializable;

public class NewsReporter extends AbstractLoggingActor {

	private final NewsEventsProducingService newsProducingService;

	private NewsReporter(NewsEventsProducingService newsProducingService) {
		this.newsProducingService = newsProducingService;
		receive(ReceiveBuilder
				.match(NewsAirTimeMsg.class, this::handleMessage)
				.build());
	}

	public static Props props(NewsEventsProducingService consumingService) {
		return Props.create(NewsReporter.class, () -> new NewsReporter(consumingService));
	}

	private void handleMessage(NewsAirTimeMsg message) {
		NewsEvent newsEvents = newsProducingService.getNewsEvents();
		presentNews(newsEvents);
	}

	private void presentNews(NewsEvent newsEvents) {
		newsEvents.accept(new NewsEvent.Visitor() {
			@Override
			public void visit(LooseCartNewsEvent event) {
				sender().tell(new RollercoasterFearGrowthMsg(), self());
			}

			@Override
			public void visit(BrokenPipeNewsEvent event) {
				sender().tell(new RollercoasterFearGrowthMsg(), self());
			}

			@Override
			public void visit(TornadoAlertNewsEvent event) {
				sender().tell(new IncomingStormMsg(), self());
			}
		});
	}

	public interface NewsTopicMsg extends Serializable {
	}

	@Value
	public static class NewsAirTimeMsg implements Serializable {
	}

	@Value
	public static class IncomingStormMsg implements NewsTopicMsg {
	}

	@Value
	public static class RollercoasterFearGrowthMsg implements NewsTopicMsg {
	}
}
