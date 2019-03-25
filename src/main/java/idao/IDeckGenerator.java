package idao;

import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.Deck;

public interface IDeckGenerator {

	public ArrayList<Card> getCards();
	
	public void saveDeck(Deck deck);
	
	public boolean deckExist(String nameDeck);
	
	public Deck getDeck(String nameDeck);
	
	public void updateDeck(Deck deck);
	
	
	
	
	
}
