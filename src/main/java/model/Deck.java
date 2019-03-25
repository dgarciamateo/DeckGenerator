package model;

import java.util.ArrayList;
import java.util.List;

public class Deck {

	
	private String deckName;
	private int deckValue;
	private ArrayList<Card> deckCards;
	
	public Deck(String deckName, int deckValue, ArrayList<Card> deckCards) {
		super();
		this.deckName = deckName;
		this.deckValue = deckValue;
		this.deckCards = deckCards;
	}

	public Deck() {
		super();
	}

	public String getDeckName() {
		return deckName;
	}

	public void setDeckName(String deckName) {
		this.deckName = deckName;
	}

	public int getDeckValue() {
		return deckValue;
	}

	public void setDeckValue(int deckValue) {
		this.deckValue = deckValue;
	}

	public ArrayList<Card> getDeckCards() {
		return deckCards;
	}

	public void setDeckCards(ArrayList<Card> deckCards) {
		this.deckCards = deckCards;
	}
	
	
	
	
	
	
}
