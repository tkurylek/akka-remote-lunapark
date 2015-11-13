package pl.kurylek.lunapark.news.report.event;

import static pl.kurylek.lunapark.news.report.event.Headline.ROLLERCOASTER_LOOSE_CART;

public class LooseCartNewsEvent extends NewsEvent {

	public LooseCartNewsEvent() {
		super(ROLLERCOASTER_LOOSE_CART);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
