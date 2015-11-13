package pl.kurylek.lunapark.news.report.event;

public abstract class NewsEvent {

	private final Headline headline;

	public NewsEvent(Headline headline) {
		this.headline = headline;
	}

	public Headline getHeadline() {
		return headline;
	}

	public abstract void accept(Visitor visitor);

	public interface Visitor {
		void visit(LooseCartNewsEvent event);

		void visit(BrokenPipeNewsEvent event);

		void visit(TornadoAlertNewsEvent event);
	}
}
