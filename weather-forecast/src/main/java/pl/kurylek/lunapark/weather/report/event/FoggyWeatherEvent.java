package pl.kurylek.lunapark.weather.report.event;

public class FoggyWeatherEvent extends WeatherEvent {

	public FoggyWeatherEvent() {
		super(Weather.FOGGY);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
