package pl.kurylek.lunapark.news.report.event;

public class TornadoAlertNewsEvent extends NewsEvent {

	public TornadoAlertNewsEvent() {
		super(Headline.TORNADO_ALERT);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
