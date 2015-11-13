package pl.kurylek.lunapark.news.report.event;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class NewsEventsProducingService {

	private RestTemplate restTemplate = new RestTemplate();
	private Random random = new Random();

	public NewsEvent getNewsEvents() {
		// restTemplate.getForObject(...)
		return randomNews();
	}

	private NewsEvent randomNews() {
		List<NewsEvent> newsHeadlines = Arrays.asList(
				new TornadoAlertNewsEvent(),
				new BrokenPipeNewsEvent(),
				new LooseCartNewsEvent());
		return newsHeadlines.get(random.nextInt(outOfBoundsPossibility(newsHeadlines)));
	}

	private int outOfBoundsPossibility(List<NewsEvent> newsHeadlines) {
		return newsHeadlines.size() + 1;
	}
}
