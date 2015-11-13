package pl.kurylek.lunapark.weather.report.event;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class WeatherEventsProducingService {

	private RestTemplate restTemplate = new RestTemplate();
	private Random random = new Random();

	public WeatherEvent getWeatherEvent() {
		// restTemplate.getForObject(...)
		return randomWeather();
	}

	private WeatherEvent randomWeather() {
		List<WeatherEvent> weatherStatuses = Arrays.asList(
				new RainyWeatherEvent(),
				new SunnyWeatherEvent(),
				new FoggyWeatherEvent());
		return weatherStatuses.get(random.nextInt(outOfBoundsPossibility(weatherStatuses)));
	}

	private int outOfBoundsPossibility(List<WeatherEvent> weatherStatuses) {
		return weatherStatuses.size() + 1;
	}
}
