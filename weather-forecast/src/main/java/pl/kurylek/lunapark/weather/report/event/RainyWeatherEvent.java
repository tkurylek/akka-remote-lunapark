package pl.kurylek.lunapark.weather.report.event;

public class RainyWeatherEvent extends WeatherEvent {

	public RainyWeatherEvent() {
		super(Weather.RAINY);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
