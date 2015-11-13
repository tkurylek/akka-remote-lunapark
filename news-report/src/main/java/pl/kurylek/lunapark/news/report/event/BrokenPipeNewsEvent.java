package pl.kurylek.lunapark.news.report.event;

public class BrokenPipeNewsEvent extends NewsEvent {

	public BrokenPipeNewsEvent() {
		super(Headline.ROLLERCOASTER_BROKEN_PIPE);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
