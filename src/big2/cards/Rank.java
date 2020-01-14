package big2.cards;

public enum Rank {
	A("A"),
	R2("2"),
	R3("3"),
	R4("4"),
	R5("5"),
	R6("6"),
	R7("7"),
	R8("8"),
	R9("9"),
	R10("10"),
	J("J"),
	Q("Q"),
	K("K");

	String display;

	Rank(String display) {
		this.display = display;
	}

	@Override
	public String toString() {
		return display;
	}
}
