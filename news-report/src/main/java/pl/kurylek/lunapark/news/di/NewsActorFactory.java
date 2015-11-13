package pl.kurylek.lunapark.news.di;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import akka.routing.FromConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kurylek.lunapark.news.report.event.NewsEventsProducingService;
import pl.kurylek.lunapark.news.report.NewsReporter;

@Service
public class NewsActorFactory {

	private NewsEventsProducingService newsEventsProducingService;

	@Autowired
	public NewsActorFactory(NewsEventsProducingService newsEventsProducingService) {
		this.newsEventsProducingService = newsEventsProducingService;
	}

	public ActorRef createNewsSensibleUnits(ActorContext context) {
		return context.actorOf(FromConfig.getInstance().props(), "news-sensibles");
	}

	public ActorRef createNewsReportingActor(ActorContext context) {
		return context.actorOf(NewsReporter.props(newsEventsProducingService), "news-reporter");
	}

	public Scheduler scheduler(ActorSystem system) {
		return system.scheduler();
	}
}
