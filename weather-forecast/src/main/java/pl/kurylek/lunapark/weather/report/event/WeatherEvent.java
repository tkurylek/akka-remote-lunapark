package pl.kurylek.lunapark.weather.report.event;

public abstract class WeatherEvent {

	private final Weather weather;

	public WeatherEvent(Weather weather) {
		this.weather = weather;
	}

	public Weather getWeather() {
		return weather;
	}

	public abstract void accept(WeatherEvent.Visitor visitor);

	public interface Visitor {
		void visit(FoggyWeatherEvent event);

		void visit(SunnyWeatherEvent event);

		void visit(RainyWeatherEvent event);
	}
}
