package big2.cards;

public enum Suit {
	CLUB('\u2663'),
	DIAMOND('\u2666'),
	HEART('\u2665'),
	SPADE('\u2660');

	char display;

	Suit(char display) {
		this.display = display;
	}

	@Override
	public String toString() {
		return String.valueOf(display);
	}
}
