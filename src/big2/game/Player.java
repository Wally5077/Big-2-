package big2.game;

import big2.cards.Card;
import big2.cards.CardGroup;

import java.util.Objects;

public class Player {
	private int id;
	private String name;
	private HandCards handCards;

	public Player(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("The player's name cannot be null or empty.");
		}
		this.name = name;
	}

	public Player(int id, String name) {
		this.id = id;
		this.name = name;
	}

	void setHandCards(HandCards handCards) {
		this.handCards = handCards;
	}

	public boolean containsHandCards(CardGroup cardGroup) {
		return handCards.contains(cardGroup);
	}

	public boolean hasEmptyHandCards() {
		return handCards.isEmpty();
	}

	public void removeHandCards(Card[] cards) {
		handCards = handCards.exclude(cards);
	}

	public HandCards getHandCards() {
		return handCards;
	}

	void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Player player = (Player) o;
		return id == player.id &&
				name.equals(player.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public String toString() {
		return "Player{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
