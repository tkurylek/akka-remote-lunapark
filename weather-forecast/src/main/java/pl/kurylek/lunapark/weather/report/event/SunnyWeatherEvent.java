package pl.kurylek.lunapark.weather.report.event;

public class SunnyWeatherEvent extends WeatherEvent {

	public SunnyWeatherEvent() {
		super(Weather.SUNNY);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
