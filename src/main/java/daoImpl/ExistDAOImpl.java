package daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import com.google.gson.Gson;

import idao.IDeckGenerator;
import model.Card;
import model.Deck;

public class ExistDAOImpl implements IDeckGenerator {

	@SuppressWarnings("rawtypes")
	private static Class c;
	private final static String uri = "xmldb:exist://localhost:8080/exist/xmlrpc/db/";
	private final static String driver = "org.exist.xmldb.DatabaseImpl";
	private final static String collectionName = "Cards";
	private final static String fileName = "card_collection.xml";
	private static org.xmldb.api.base.Collection collec= null;
	private static XMLResource res = null;
	
	public ArrayList<Card> getCards() {

		ArrayList<Card> cards = null;
		
		try {
			c = Class.forName(driver);
			Database database = (Database) c.newInstance();
			DatabaseManager.registerDatabase(database);
			
			collec = DatabaseManager.getCollection(uri+collectionName);
			res = (XMLResource) collec.getResource(fileName);
			
			 if(res == null) {
	                System.out.println("There is no document.");
	            } else {
	                //System.out.println(res.getContent());
	                
	            	JSONObject xmlO = XML.toJSONObject((String)res.getContent());

	                JSONArray CardsCol = xmlO.getJSONObject("cards").getJSONArray("card");
	        	
	               
	                //Vaciamos el arraylist
	                cards = new ArrayList<Card>();
	                
	                for (int i = 0; i < CardsCol.length(); i++) {
						
	                	Card card = new Gson().fromJson(CardsCol.get(i).toString(), Card.class);
	                	cards.add(card);
	                	
					}
			
			
		} 
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		return cards;
		
		
	}

	public void saveDeck(List<Card> deck, String nameDeck, int valueDeck) {
		// TODO Auto-generated method stub

		
	}

	public Deck saveDeck(String nameDeck, int valueDeck, List<Card> deck) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deckExist(String nameDeck) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void saveDeck(Deck deck) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Deck getDeck(String nameDeck) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateDeck(Deck deck) {
		// TODO Auto-generated method stub
		
	}


}
