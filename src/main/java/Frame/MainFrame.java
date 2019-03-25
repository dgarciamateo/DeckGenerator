package Frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import daoImpl.ExistDAOImpl;
import daoImpl.MongoDAOImpl;
import idao.IDeckGenerator;
import model.Card;
import model.Deck;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField search;
	private boolean hasClicked = false;
	private JButton b_add;
	private ArrayList<Card> cards = null;
	private int value, vAux;
	private String name = "Deck";
	private boolean hasFail = false;

	private DefaultListModel<Card> model;
	private DefaultListModel<Card> model2;
	private JList<Card> Deck_list;
	private JList<Card> Col_list;

	final JButton b_random = new JButton("RANDOM");

	final IDeckGenerator gestorExist = new ExistDAOImpl();
	final IDeckGenerator gestorMongo = new MongoDAOImpl();

	// Arrays que almacenan las cartas de ambas listas, tanto de la Collection como
	// de Deck
	private ArrayList<Card> collectionCards = new ArrayList<Card>();
	private ArrayList<Card> deckCards = new ArrayList<Card>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 540, 349);

		// Le ponemos un fondo al frame, aplicando una imagen al content pane
		ImageIcon icon = new ImageIcon("src/main/resources" + File.separator + "wp.jpg");
		final Image image = icon.getImage();
		contentPane = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
			}
		};

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		// Estilo del JList

		model = new DefaultListModel<Card>();

		Col_list = new JList<Card>(model);
		Col_list.setCellRenderer(new TransparentListCellRenderer());
		Col_list.setOpaque(false);
		Col_list.setSelectionBackground(new Color(109, 14, 118));

		JScrollPane scrollPane = new JScrollPane(Col_list);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);

		model2 = new DefaultListModel<Card>();

		Deck_list = new JList<Card>(model2);
		Deck_list.setCellRenderer(new TransparentListCellRenderer());
		Deck_list.setOpaque(false);
		Deck_list.setSelectionBackground(new Color(109, 14, 118));

		JScrollPane scrollPane2 = new JScrollPane(Deck_list);
		scrollPane2.setOpaque(false);
		scrollPane2.getViewport().setOpaque(false);

		final JLabel lblBaraja = new JLabel(name + " - Total Value: " + value);
		lblBaraja.setForeground(Color.WHITE);
		lblBaraja.setFont(new Font("Times New Roman", Font.BOLD, 18));

		/*
		 * BOTON ADD
		 */

		ImageIcon icon2 = new ImageIcon("src/main/resources" + File.separator + "Add.png");

		JButton b_add = new JButton();
		b_add.setIcon(icon2);
		b_add.setBackground(new Color(119, 50, 17));
		b_add.setMargin(new Insets(0, 0, 0, 0));

		b_add.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				// Cogemos los elementos seleccionados y los metemos en la lista de Strings
				// selected

				List<Card> selected = Col_list.getSelectedValuesList();
				hasFail = false;

				for (int i = 0; i < selected.size(); i++) {
					// Coger el value de la carta y sumarlo al total
					Card cardSelec = selected.get(i);

					if (value <= 20) {
						vAux = cardSelec.getValue();

						// Comprobamos que la suma del valor total y el ultimo vaor que vaya a ser
						// insertado no superan 20
						if (value + vAux <= 20) {
							// Metemos la seleccion en la lista deck y la quitamos de la lista collection
							model2.addElement(cardSelec);
							model.removeElement(cardSelec);

							value = value + vAux;
							lblBaraja.setText(name + " - Total Value: " + value);
							// Metemos la carta seleccionada en el arraylist de cartas de la baraja nueva
							deckCards.add(cardSelec);

							// Bucle que busca la carta que se ha seleccionado para añadir y se borra del
							// arraylist de cartas de la collection
							for (int j = 0; j < collectionCards.size(); j++) {
								if (collectionCards.get(j).getName().equals(cardSelec.getName())) {
									collectionCards.remove(j);
								}
							}

						} else {

							// Variable semaforo para controllar que solo salte el error una vez si hay
							// muchas selecciones a la vez
							if (!hasFail) {
								JOptionPane.showMessageDialog(null,
										"The Card you want to add will surpass the total deck value.", "Error",
										JOptionPane.ERROR_MESSAGE);
								hasFail = true;
							}

						}

					} else {

					}

				}

			}
		});

		/*
		 * BOTON QUIT
		 */

		ImageIcon icon3 = new ImageIcon("src/main/resources" + File.separator + "Quit.png");

		JButton b_quit = new JButton();
		b_quit.setIcon(icon3);
		b_quit.setBackground(new Color(119, 50, 17));
		b_quit.setMargin(new Insets(0, 0, 0, 0));

		b_quit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

				List<Card> selected = Deck_list.getSelectedValuesList();

				for (int i = 0; i < selected.size(); i++) {

					Card cardSelec = selected.get(i);

					value = value - cardSelec.getValue();

					lblBaraja.setText(name + " - Total Value: " + value);
					model.addElement(selected.get(i));
					model2.removeElement(selected.get(i));

					// Metemos la carta seleccionada de vuelta en el arraylist de cartas de la
					// collection
					collectionCards.add(cardSelec);

					// Bucle que busca la carta que se ha seleccionado se borra del arraylist de
					// cartas de la baraja
					for (int j = 0; j < deckCards.size(); j++) {
						if (deckCards.get(j).getName().equals(cardSelec.getName())) {
							deckCards.remove(j);
						}
					}

				}

			}
		});

		JLabel lblColeccion = new JLabel("Collection");
		lblColeccion.setForeground(Color.WHITE);
		lblColeccion.setBackground(new Color(139, 69, 19));
		lblColeccion.setFont(new Font("Times New Roman", Font.BOLD, 18));

		/*
		 * BOTON RANDOM
		 */

		b_random.setEnabled(false);
		b_random.setForeground(Color.WHITE);
		b_random.setBackground(new Color(119, 50, 17));

		b_random.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

				deckCards.removeAll(deckCards);
				model2.removeAllElements();
				model.removeAllElements();

				// NOTA: para actualizar al momento poner linia abajo con esto: cards =
				// gestor.getCards();
				for (int i = 0; i < cards.size(); i++) {

					Card card = new Card(0, cards.get(i).getName(), cards.get(i).getSummonCost(),
							cards.get(i).getAttack(), cards.get(i).getDefense(), cards.get(i).getValue());

					model.add(i, card);

				}

				model2.removeAllElements();
				value = 0;
				name = "Deck";
				lblBaraja.setText(name + " - Total Value: " + value);
				lblBaraja.setForeground(Color.WHITE);

				while (value <= 20) {

					int random = (int) (Math.random() * Col_list.getModel().getSize());

					Card cardSelec = Col_list.getModel().getElementAt(random);

					if (value + cardSelec.getValue() <= 20) {
						value = value + cardSelec.getValue();

						lblBaraja.setText(name + " - Total Value: " + value);
						model2.addElement(Col_list.getModel().getElementAt(random));
						model.remove(random);

						deckCards.add(cardSelec);
					} else {
						break;

					}
				}
			}
		});

		/*
		 * BOTON LOAD
		 */

		JButton b_load = new JButton("LOAD");
		b_load.setForeground(Color.WHITE);
		b_load.setBackground(new Color(119, 50, 17));

		b_load.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

				cards = gestorExist.getCards();
				b_random.setEnabled(true);
				model.removeAllElements();
				model2.removeAllElements();
				value = 0;
				name = "Deck";
				lblBaraja.setText(name + " - Total Value: " + value);
				lblBaraja.setForeground(Color.WHITE);

				// NOTA: para actualizar al momento poner linia abajo con esto: cards =
				// gestor.getCards();
				for (int i = 0; i < cards.size(); i++) {
					
					Card cardLoad = cards.get(i);


					collectionCards.add(cardLoad);

					model.add(i, cardLoad);

				}

			}
		});

		/*
		 * BOTON SAVE
		 */

		JButton b_save = new JButton("SAVE");
		b_save.setForeground(Color.WHITE);
		b_save.setBackground(new Color(119, 50, 17));

		b_save.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

				if (value > 20) {

					JOptionPane.showMessageDialog(null, "Deck`s Total Value cannot be higher than 20.", "Error",
							JOptionPane.ERROR_MESSAGE);

				} else if (value == 0) {

					JOptionPane.showMessageDialog(null, "The Deck cannot be empty.", "Error",
							JOptionPane.ERROR_MESSAGE);

				} else {
					// Si el mazo se llama Deck significara que es el mazo sin guardar y por lo
					// tanto se desea guardar el nuevo mazo, si no es asi, y el nombre es otro
					// significa que el usuario desea modificar un mazo ya cargado de la base de
					// datoa de mongo
					if (name.equals("Deck")) {

						String deckName = "";

						boolean existDeck = true;

						// Bucle que comprueba si el mazo ya existe, si existe vuelve a pedirle un
						// nombre hasta que ponga uno disponible en la BBDD
						while (existDeck == true) {
							deckName = (JOptionPane.showInputDialog("Insert a name for your Deck."));

							existDeck = gestorMongo.deckExist(deckName);

							if (existDeck == true) {

								JOptionPane.showMessageDialog(null, "The Deck " + deckName + " has already exist.",
										"Name Exist Error", JOptionPane.ERROR_MESSAGE);

							} else {
								break;
							}

						}

						Deck deck = new Deck(deckName, value, deckCards);

						gestorMongo.saveDeck(deck);

						JOptionPane.showMessageDialog(null,
								"The Deck " + deckName + " has inserted correctly to the Mongo database.", "Info",
								JOptionPane.INFORMATION_MESSAGE);

					} else {

						Deck deck = new Deck(name, value, deckCards);

						gestorMongo.updateDeck(deck);

						JOptionPane.showMessageDialog(null, "The Deck " + name + " has updated correctly.", "Info",
								JOptionPane.INFORMATION_MESSAGE);

					}

				}

			}
		});

		/*
		 * BOTON SEARCH
		 */

		JButton b_search = new JButton("SEARCH & LOAD");
		b_search.setBackground(new Color(119, 50, 17));
		b_search.setForeground(Color.white);

		search = new JTextField();
		search.setColumns(10);

		b_search.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

				
				if (!search.getText().equals("")) {
					boolean existDeck = gestorMongo.deckExist(search.getText());

					if (existDeck) {

						
						cards = gestorExist.getCards();
						b_random.setEnabled(true);
						model.removeAllElements();
						model2.removeAllElements();
						value = 0;
						name = "Deck";
						lblBaraja.setText(name + " - Total Value: " + value);
						lblBaraja.setForeground(Color.WHITE);
						
						for (int i = 0; i < cards.size(); i++) {
							
							Card cardLoad = cards.get(i);


							collectionCards.add(cardLoad);

							model.add(i, cardLoad);

						}
						//METODO PARA QUE CARGUE TODAS LAS CARTAS ^^
						
						Deck deckSearch = gestorMongo.getDeck(search.getText());
						

						ArrayList<Card> cardsDeck = deckSearch.getDeckCards();
						vAux = 0;

						for (int i = 0; i < cardsDeck.size(); i++) {

							vAux = vAux + cardsDeck.get(i).getValue();

						}

						for (int i = 0; i < cardsDeck.size(); i++) {

							Card cardDeck = cardsDeck.get(i);

							model2.add(i, cardDeck);
						}

						// Esto es para eliminar las cartas de la lista Collection que ya estan en la
						// lista Deck
						for (int i = 0; i < model.size(); i++) {

							for (int j = 0; j < model2.size(); j++) {

								if (model.get(i).equals(model2.get(j))) {
//									System.out.println(model.get(i)+" = "+model2.get(j));
									model.removeElementAt(i);
								}
							}
						}

						value = vAux;
						name = deckSearch.getDeckName();
						lblBaraja.setText(name + " - Total Value: " + value);

					} else {

						JOptionPane.showMessageDialog(null, "The Deck you are trying to search is not in the database.",
								"Not Found Error", JOptionPane.ERROR_MESSAGE);

					}

				} else {

					JOptionPane.showMessageDialog(null, "The search is empty.", "Empty Search Error",
							JOptionPane.WARNING_MESSAGE);

				}

			}
		});

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(71)
						.addComponent(lblColeccion, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(146)
						.addComponent(lblBaraja, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(165))
				.addGroup(gl_contentPane.createSequentialGroup().addGap(47)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(b_load, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE).addComponent(
										scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(b_quit, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
								.addComponent(b_add, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(b_save, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE).addComponent(
										scrollPane2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup().addGap(8).addComponent(search,
										GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(b_random, GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
												.addComponent(b_search, GroupLayout.DEFAULT_SIZE, 171,
														Short.MAX_VALUE))))
						.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addGap(15)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblColeccion, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblBaraja, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(27)
								.addComponent(b_add, GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(b_quit, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE).addGap(40))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addGroup(gl_contentPane
								.createSequentialGroup().addGap(11)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
										.addGroup(gl_contentPane.createSequentialGroup().addGap(7).addComponent(
												scrollPane2, GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE))))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(search, GroupLayout.PREFERRED_SIZE, 20,
												GroupLayout.PREFERRED_SIZE)
										.addGap(11).addComponent(b_search)
										.addPreferredGap(ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
										.addComponent(b_random).addGap(51))))
				.addGap(11).addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(b_load)
						.addComponent(b_save, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
				.addGap(20)));

		contentPane.setLayout(gl_contentPane);
	}

	public void loadAllCards() {

//		// METODO PARA QUE CARGUE TODAS LAS CARTAS
//		cards = gestorExist.getCards();
//		b_random.setEnabled(true);
//		model.removeAllElements();
//		model2.removeAllElements();
//		value = 0;
//		name = "Deck";
//		lblBaraja.setText(name + " - Total Value: " + value);
//		lblBaraja.setForeground(Color.WHITE);
//
//		// NOTA: para actualizar al momento poner linia abajo con esto: cards =
//		// gestor.getCards();
//		for (int i = 0; i < cards.size(); i++) {
//
//			String text = cards.get(i).getName() + " - " + cards.get(i).getSummonCost() + " - "
//					+ cards.get(i).getAttack() + " - " + cards.get(i).getDefense() + " - " + cards.get(i).getValue();
//
//			collectionCards.add(new Card(cards.get(i).getId(), cards.get(i).getName(), cards.get(i).getSummonCost(),
//					cards.get(i).getAttack(), cards.get(i).getDefense(), cards.get(i).getValue()));
//
//			model.add(i, text);
//
//		}
//		// METODO PARA QUE CARGUE TODAS LAS CARTAS EN LA LISTA DE COLLECTION^^

	}

	public class TransparentListCellRenderer extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			setForeground(Color.WHITE);
			setOpaque(isSelected);
			return this;
		}

	}
}
