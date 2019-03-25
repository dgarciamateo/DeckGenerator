package daoImpl;

import java.util.ArrayList;
import java.util.Iterator;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.UpdateResult;

import idao.IDeckGenerator;
import model.Card;
import model.Deck;

public class MongoDAOImpl implements IDeckGenerator{

	private MongoClientURI connector;
	private MongoClient con;
	private MongoDatabase  database;
	private MongoCollection<Document> collection;
	
	
	@Override
	public ArrayList<Card> getCards() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deckExist(String nameDeck) {
		// TODO Auto-generated method stub
		boolean exist = false;
		
		//Le decimos la collection en la que queremos trabajar, si no esta creada la crea esta misma sentencia al ejecutarse
		con = new MongoClient("localhost", 27017); 
		//Assginamos en la DataBase que operaremos
		database = con.getDatabase("DeckGeneratorM6");
		//Le decimos la collection en la que queremos trabajar, si no esta creada la crea esta misma sentencia al ejecutarse
		collection = database.getCollection("Decks");
		
//		org.bson.Document document = collection.find(Filters.eq("deckName", nameDeck)).projection(Projections.excludeId()).first();
//		MongoCursor<Document> cursor = collection.find(Filters.eq("deckName",nameDeck)).iterator();
		
		MongoCursor<Document> cursor = collection.find(Filters.eq("deckName", nameDeck)).iterator();


		
		try {
			Document document = cursor.next();
			
			Gson gson = new Gson();
			// Convertir el documento a JSON
			String json = document.toJson();
			// Convertir el JSON a baraja
			Deck deck = gson.fromJson(json, Deck.class);
			
			if(deck.equals(null)) {
				exist=false;
			}else {
				exist=true;
			}
				
		}catch(Exception e) {
			
		}
		
		con.close();
		collection=null;
		database=null;

		
		
		return exist;
	}

	@Override
	public void saveDeck(Deck deck) {
		// TODO Auto-generated method stub
		
		
		con = new MongoClient("localhost", 27017); 
		//Assginamos en la DataBase que operaremos
		database = con.getDatabase("DeckGeneratorM6");
		//Le decimos la collection en la que queremos trabajar, si no esta creada la crea esta misma sentencia al ejecutarse
		collection = database.getCollection("Decks");
		
		ArrayList<Object> cardsDeck = new BasicDBList();
		
		//creamos el Document con el que insertaremos los datos
		Document document = new Document();
		
		//Introducimos tanto el nombre como el valor de la baraja
		document.put("deckName", deck.getDeckName());
		document.put("deckValue", deck.getDeckValue());
		
		//Bucle para introducir carta por carta en la BBDD
		for (int i = 0; i < deck.getDeckCards().size(); i++) {
			
			DBObject object = new BasicDBObject();
			
			object.put("id", deck.getDeckCards().get(i).getId());
			object.put("name", deck.getDeckCards().get(i).getName());
			object.put("summonCost", deck.getDeckCards().get(i).getSummonCost());
			object.put("attack", deck.getDeckCards().get(i).getAttack());
			object.put("defense", deck.getDeckCards().get(i).getDefense());
			object.put("value", deck.getDeckCards().get(i).getValue());
			
			cardsDeck.add(object);
		}
		
		//Insertamos los datos en la collection
		document.put("deckCards", cardsDeck);
		collection.insertOne(document);
		
		
		//Para desconectar y cerrar la conexion con la BBDD de mongo
		collection=null;
		database=null;
		con.close();
		
	}

	@Override
	public Deck getDeck(String nameDeck) {
		// TODO Auto-generated method stub
		con = new MongoClient("localhost", 27017); 
		//Assginamos en la DataBase que operaremos
		database = con.getDatabase("DeckGeneratorM6");
		//Le decimos la collection en la que queremos trabajar, si no esta creada la crea esta misma sentencia al ejecutarse
		collection = database.getCollection("Decks");
		Deck deck = null;

		MongoCursor<Document> cursor = collection.find(Filters.eq("deckName", nameDeck)).iterator();

		try {
			Document document = cursor.next();
			deck = new Gson().fromJson(document.toJson(), Deck.class);
		} catch (Exception e) {

		}

		con.close();
		connector = null;
		collection = null;
		database = null;

		return deck;
	}

	@Override
	public void updateDeck(Deck deck) {
		// TODO Auto-generated method stub
		
		con = new MongoClient("localhost", 27017); 
		//Assginamos en la DataBase que operaremos
		database = con.getDatabase("DeckGeneratorM6");
		//Le decimos la collection en la que queremos trabajar, si no esta creada la crea esta misma sentencia al ejecutarse
		collection = database.getCollection("Decks");
		
		ObjectMapper mapper = new ObjectMapper();
		String deckJson = null;
		try {
			//pasamos el mazo a Json
			deckJson = mapper.writeValueAsString(deck);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		//Realizamos la actualizacion
		Document userDoc = Document.parse(deckJson);
		collection.replaceOne(Filters.eq("deckName", deck.getDeckName()), userDoc);
		
		collection=null;
		database=null;
		con.close();
		
		
	}
	
	
	
}
