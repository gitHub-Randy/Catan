package view;

import controller.Board;
import controller.Catan;
import controller.Dice;
import controller.Location;
import controller.Tile;
import model.CatanModel;
import model.City;
import model.Road;
import model.Village;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Set;

@SuppressWarnings("serial")
public class BoardView extends JPanel implements MouseMotionListener, MouseListener {

	/*
	 * De road kan nog steeds de lengte hebben van een coordinaat Dit kan gefixed
	 * worden oor het een minimale lengte hebben van minimaal 2 coordinaten
	 */

	// instance variables
	private Board boardController;
	private Polygon hex;

	private ClickPoints klikpunten;

	private int panelXmargin;
	private int panelYmargin;
	private int hexagonWidth;
	private int hexagonYdistance;
	private int robbert;
	private int buildOption;
	private boolean playDevelopmentCard = false;

	private int xKey;
	private int yKey;
	private static final Color wool = new Color(140, 254, 101);
	private static final Color forest = new Color(2, 174, 0);
	private static final Color grain = new Color(255, 255, 26);
	private static final Color clay = new Color(172, 115, 57);
	private static final Color ore = new Color(115, 115, 115);
	private static final Color darude = new Color(244, 233, 144);
	private static final Color seaColor = new Color(136, 206, 250);
	private String[] middlePointStringArray;

	private JLabel[] valueLabel;

	private boolean beginPointIsMiddlePoint;
	private boolean endPointIsMiddlePoint;

	private ArrayList<Point> villagePoints;
	private ArrayList<Point> roadBeginPoints;
	private ArrayList<Point> roadEndPoints;
	private ArrayList<Point> cityPoints;

	private SelfViewWithButtons selfViewWithButtons;

	private Catan catanController;
	private Dice dice;

	private ArrayList<Point> user1Points;
	private ArrayList<Point> user1RoadEndPoints;
	private ArrayList<Point> user2Points;
	private ArrayList<Point> user2RoadEndPoints;
	private ArrayList<Point> user3Points;
	private ArrayList<Point> user3RoadEndPoints;
	private ArrayList<Point> user4Points;
	private ArrayList<Point> user4RoadEndPoints;

	public BoardView(Catan catanController, CatanView catanView) {
		setBackground(seaColor);
		setLayout(null);
		setPreferredSize(new Dimension(300, 500));

		this.catanController = catanController;
		dice = catanController.getDice();
		boardController = catanController.getBoard();
		klikpunten = new ClickPoints();

		selfViewWithButtons = catanView.getSelfView();
		panelXmargin = 58;
		panelYmargin = 185;
		hexagonYdistance = 15;
		hexagonWidth = 58;
		middlePointStringArray = createMiddleCoordinatesStringArray();
		createKlikPuntenArrays();

		villagePoints = new ArrayList<>();
		roadBeginPoints = new ArrayList<>();
		roadEndPoints = new ArrayList<>();
		cityPoints = new ArrayList<>();

		beginPointIsMiddlePoint = false;
		endPointIsMiddlePoint = false;

		hex = new Polygon();
		valueLabel = new JLabel[19];

		user1Points = new ArrayList<>();
		user1RoadEndPoints = new ArrayList<>();
		user2Points = new ArrayList<>();
		user2RoadEndPoints = new ArrayList<>();
		user3Points = new ArrayList<>();
		user3RoadEndPoints = new ArrayList<>();
		user4Points = new ArrayList<>();
		user4RoadEndPoints = new ArrayList<>();
	}

	// maakt een Strign array met alle DB middelpunten
	private String[] createMiddleCoordinatesStringArray() {
		return new String[] { "2,4", "3,6", "4,8", "3,3", "4,5", "5,7", "6,9", "4,2", "5,4", "6,6", "7,8", "8,10",
				"6,3", "7,5", "8,7", "9,9", "8,4", "9,6", "10,8" };
	}

	private void createKlikPuntenArrays() {
		createKlikpunten();
		createKlikPuntenWithoutMP();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		drawAndFillHexes(g2d);
		drawHarbours(g2d);
		buildCities(g2d);
		buildVillages(g2d);
		buildRoads(g2d);
		drawRobber(g2d);
	}

	public void setRobbert(int robbertLocation) {
		robbert = robbertLocation;
	}

	private void drawAndFillHexes(Graphics2D g2D) {
		// Hexagon plus kleur en getallenfiches
		for (int i = 0; i < middlePointStringArray.length; i++) {
			Tile tile = boardController.getTiles2(i);
			char resourceType = tile.getResourceType();
			int valueNumber = tile.getValue();

			switch (resourceType) {
			case 'W':
				g2D.setColor(wool);
				break;
			case 'G':
				g2D.setColor(grain);
				break;
			case 'B':
				g2D.setColor(clay);
				break;
			case 'E':
				g2D.setColor(ore);
				break;
			case 'H':
				g2D.setColor(forest);
				break;
			case 'X':
				g2D.setColor(darude);
				break;
			default:
				break;
			}

			g2D.fillPolygon(hexagon(middlePointStringArray[i]));
			valueLabel[i] = new JLabel(Integer.toString(valueNumber));
			valueLabel[i].setBounds(convertXfromKeyToScreenX(middlePointStringArray[i]),
					convertYfromKeyToScreenY(middlePointStringArray[i]), 80, 30);
			add(valueLabel[i]);
			hex.reset();
		}

		// Hexagon rand
		for (String aMiddlePointStringArray : middlePointStringArray) {
			g2D.setColor(Color.BLACK);
			g2D.drawPolygon(hexagon(aMiddlePointStringArray));
			hex.reset();
		}
	}

	private void drawHarbours(Graphics2D g2D) {
		Point h = new Point();
		h.setLocation(convertXfromKeyToScreenX("2,5"), convertYfromKeyToScreenY("2,5"));
		Point h2 = new Point();
		h2.setLocation(convertXfromKeyToScreenX("2,6"), convertYfromKeyToScreenY("2,6"));
		Stroke harbour = new BasicStroke(3f);
		g2D.setStroke(harbour);
		g2D.setColor(forest);
		g2D.drawLine(h.x - 10, h.y, h2.x - 10, h2.y);

		Point h3 = new Point();
		h3.setLocation(convertXfromKeyToScreenX("3,8"), convertYfromKeyToScreenY("3,8"));
		Point h4 = new Point();
		h4.setLocation(convertXfromKeyToScreenX("4,9"), convertYfromKeyToScreenY("4,9"));
		g2D.setStroke(harbour);
		g2D.setColor(Color.black);
		g2D.drawLine(h3.x - 5, h3.y - 10, h4.x - 5, h4.y - 10);

		Point h5 = new Point();
		h5.setLocation(convertXfromKeyToScreenX("6,10"), convertYfromKeyToScreenY("6,10"));
		Point h6 = new Point();
		h6.setLocation(convertXfromKeyToScreenX("7,10"), convertYfromKeyToScreenY("7,10"));
		g2D.setStroke(harbour);
		g2D.setColor(grain);
		g2D.drawLine(h5.x + 5, h5.y - 10, h6.x + 5, h6.y - 10);

		Point h7 = new Point();
		h7.setLocation(convertXfromKeyToScreenX("9,10"), convertYfromKeyToScreenY("9,10"));
		Point h8 = new Point();
		h8.setLocation(convertXfromKeyToScreenX("10,10"), convertYfromKeyToScreenY("10,10"));
		g2D.setStroke(harbour);
		g2D.setColor(ore);
		g2D.drawLine(h7.x + 5, h7.y - 10, h8.x + 5, h8.y - 10);

		Point h9 = new Point();
		h9.setLocation(convertXfromKeyToScreenX("11,9"), convertYfromKeyToScreenY("11,9"));
		Point h10 = new Point();
		h10.setLocation(convertXfromKeyToScreenX("11,8"), convertYfromKeyToScreenY("11,8"));
		g2D.setStroke(harbour);
		g2D.setColor(Color.BLACK);
		g2D.drawLine(h9.x + 10, h9.y, h10.x + 10, h10.y);

		Point h11 = new Point();
		h11.setLocation(convertXfromKeyToScreenX("10,6"), convertYfromKeyToScreenY("10,6"));
		Point h12 = new Point();
		h12.setLocation(convertXfromKeyToScreenX("9,5"), convertYfromKeyToScreenY("9,5"));
		g2D.setStroke(harbour);
		g2D.setColor(wool);
		g2D.drawLine(h11.x + 5, h11.y + 10, h12.x + 5, h12.y + 10);

		Point h13 = new Point();
		h13.setLocation(convertXfromKeyToScreenX("6,2"), convertYfromKeyToScreenY("6,2"));
		Point h14 = new Point();
		h14.setLocation(convertXfromKeyToScreenX("7,3"), convertYfromKeyToScreenY("7,3"));
		g2D.setStroke(harbour);
		g2D.setColor(Color.BLACK);
		g2D.drawLine(h13.x + 5, h13.y + 10, h14.x + 5, h14.y + 10);

		Point h15 = new Point();
		h15.setLocation(convertXfromKeyToScreenX("3,1"), convertYfromKeyToScreenY("3,1"));
		Point h16 = new Point();
		h16.setLocation(convertXfromKeyToScreenX("4,1"), convertYfromKeyToScreenY("4,1"));
		g2D.setStroke(harbour);
		g2D.setColor(Color.BLACK);
		g2D.drawLine(h15.x - 5, h15.y + 10, h16.x - 5, h16.y + 10);

		Point h17 = new Point();
		h17.setLocation(convertXfromKeyToScreenX("2,3"), convertYfromKeyToScreenY("2,3"));
		Point h18 = new Point();
		h18.setLocation(convertXfromKeyToScreenX("2,2"), convertYfromKeyToScreenY("2,2"));
		g2D.setStroke(harbour);
		g2D.setColor(clay);
		g2D.drawLine(h17.x - 10, h17.y, h18.x - 10, h18.y);

	}

	private void buildVillages(Graphics2D g2D) {
		for (Point p : villagePoints) {
			if (p.x == 0 || p.y == 0) {
				continue;
			}

			g2D.setColor(getColor(p));
			g2D.fillRect((int) p.getX() - 4, (int) p.getY(), 10, 10);
			int[] xArray = { (int) (p.getX() - 4), (int) (p.getX() + 5 - 4), (int) (p.getX() + 10 - 4) };
			int[] yArray = { (int) p.getY(), (int) (p.getY() - 10), (int) p.getY() };
			g2D.fillPolygon(new Polygon(xArray, yArray, 3));
			g2D.setColor(Color.black);
			g2D.drawPolygon(new Polygon(xArray, yArray, 3));
			g2D.drawRect((int) (p.getX() - 4), (int) p.getY(), 10, 10);
		}
	}

	public void buildCities(Graphics2D g2D) {
		for (Point p : cityPoints) {
			if (p.x == 0 || p.y == 0) {
				continue;
			}

			g2D.setColor(getColor(p));
			g2D.fillRect((int) (p.getX() - 4), (int) p.getY(), 10, 10);
			g2D.fillRect((int) (p.getX() + 6), (int) p.getY(), 10, 10);
			int[] xArray = { (int) (p.getX() - 4), (int) (p.getX() + 5 - 4), (int) (p.getX() + 10 - 4) };
			int[] yArray = { (int) p.getY(), (int) (p.getY() - 10), (int) p.getY() };
			g2D.fillPolygon(new Polygon(xArray, yArray, 3));
			g2D.setColor(Color.black);
			g2D.drawPolygon(new Polygon(xArray, yArray, 3));
			g2D.drawRect((int) (p.getX() - 4), (int) p.getY(), 10, 10);
			g2D.drawRect((int) (p.getX() + 6), (int) p.getY(), 10, 10);
		}
	}

	private void buildRoads(Graphics2D g2D) {
		int y = 0;
		int x = 0;
		for (int i = x; i < roadBeginPoints.size(); i++) {
			for (int j = y; j < roadEndPoints.size(); j++) {
				Point firstPoint = roadBeginPoints.get(i);
				Point secondPoint = roadEndPoints.get(j);

				if (firstPoint.x == 0 || firstPoint.y == 0 || secondPoint.x == 0 || secondPoint.y == 0) {
					continue;
				}

				Stroke road = new BasicStroke(3f);
				g2D.setStroke(road);
				g2D.setColor(getRoadColor(firstPoint, secondPoint));
				g2D.drawLine(firstPoint.x, (int) firstPoint.getY(), (int) secondPoint.getX(), (int) secondPoint.getY());

				y++;
				x++;
				break;
			}
		}
	}

	private Color getColor(Point point) {
		CatanModel catanModel = catanController.getModel();

		if (user1Points.contains(point)) {
			return catanModel.getUserWithIndex(0).switchColors();
		} else if (user2Points.contains(point)) {
			return catanModel.getUserWithIndex(1).switchColors();
		} else if (user3Points.contains(point)) {
			return catanModel.getUserWithIndex(2).switchColors();
		} else if (user4Points.contains(point)) {
			return catanModel.getUserWithIndex(3).switchColors();
		} else {
			return catanModel.getOwnUser().switchColors();
		}
	}

	private Color getRoadColor(Point point1, Point point2) {
		CatanModel catanModel = catanController.getModel();

		if (user1Points.contains(point1) && user1RoadEndPoints.contains(point2)) {
			return catanModel.getUserWithIndex(0).switchColors();
		} else if (user2Points.contains(point1) && user2RoadEndPoints.contains(point2)) {
			return catanModel.getUserWithIndex(1).switchColors();
		} else if (user3Points.contains(point1) && user3RoadEndPoints.contains(point2)) {
			return catanModel.getUserWithIndex(2).switchColors();
		} else if (user4Points.contains(point1) && user4RoadEndPoints.contains(point2)) {
			return catanModel.getUserWithIndex(3).switchColors();
		} else {
			return catanModel.getOwnUser().switchColors();
		}
	}

	// check of een weg niet te lang en niet te kort is
	private void checkDistance() {
		if (roadBeginPoints.size() > 0 && roadEndPoints.size() > 0) {
			int dx = (int) (roadBeginPoints.get(roadBeginPoints.size() - 1).getX()
					- roadEndPoints.get(roadEndPoints.size() - 1).getX());
			int dy = (int) (roadBeginPoints.get(roadBeginPoints.size() - 1).getY()
					- roadEndPoints.get(roadEndPoints.size() - 1).getY());
			double dis2 = Math.sqrt(dx * dx + dy * dy);

			if (dis2 > 35) {
				roadBeginPoints.remove(roadBeginPoints.size() - 1);
				roadEndPoints.remove(roadEndPoints.size() - 1);
				beginPointIsMiddlePoint = false;
				endPointIsMiddlePoint = false;

			} else if (dis2 < 30) {
				roadBeginPoints.remove(roadBeginPoints.size() - 1);
				roadEndPoints.remove(roadEndPoints.size() - 1);
			}
		}

	}

	// maakt een hashmap in Clickpoints aan die geen middelpunten bevat
	private void createKlikPuntenWithoutMP() {
		Set<String> keys2 = boardController.getLocations();

		for (String key2 : keys2) {
			klikpunten.addPointWithoutMP(convertXfromKeyToScreenX(key2), convertYfromKeyToScreenY(key2), key2);
		}

		for (String key3 : keys2) {
			for (String middlePointString : middlePointStringArray) {
				if (middlePointString.equals(key3)) {
					Point key4 = new Point();
					key4.setLocation(convertXfromKeyToScreenX(key3), convertYfromKeyToScreenY(key3));
					klikpunten.getPointsWithoutMP().remove(key4, key3);
				}
			}
		}
	}

	// maakt Points aan de hand van de db coordianten aan
	private void createKlikpunten() {
		Set<String> keys = boardController.getLocations();
		for (String key : keys) {
			klikpunten.addPoint(convertXfromKeyToScreenX(key), convertYfromKeyToScreenY(key), key);
		}
	}

	// verandert de X db coordinaat naar x Pixel waarde
	private int convertXfromKeyToScreenX(String key) {
		String[] c = key.split(",");
		int keyX = Integer.parseInt(c[0]);
		return panelXmargin + ((keyX - 1) * (hexagonWidth / 2)) + keyX - 2;
	}

	// verandert de Y db coordinaat naar Y Pixel waarde
	private int convertYfromKeyToScreenY(String key) {
		String[] c = key.split(",");
		int keyX = Integer.parseInt(c[0]);
		int keyY = Integer.parseInt(c[1]);
		return panelYmargin + (((2 * (12 - keyY)) - (10 - keyX)) * hexagonYdistance);
	}

	// verandert de pixel waardes naar DB coordinaten
	private String convertXYfromScreenToKey(int x, int y) {
		return klikpunten.getKey(x, y);
	}

	// geeft een Polygon in de vorm van een hexagon terug
	private Polygon hexagon(String cord) {
		String[] c = cord.split(",");
		int keyX = Integer.parseInt(c[0]);
		int keyY = Integer.parseInt(c[1]);

		keyY += 1;
		String currentCord = "" + keyX + "," + keyY;
		hex.addPoint(convertXfromKeyToScreenX(currentCord), convertYfromKeyToScreenY(currentCord));// Boven
		keyX += 1;
		currentCord = "" + keyX + "," + keyY;
		hex.addPoint(convertXfromKeyToScreenX(currentCord), convertYfromKeyToScreenY(currentCord));// northEast
		keyY -= 1;
		currentCord = "" + keyX + "," + keyY;
		hex.addPoint(convertXfromKeyToScreenX(currentCord), convertYfromKeyToScreenY(currentCord));// southEast
		keyX -= 1;
		keyY -= 1;
		currentCord = "" + keyX + "," + keyY;
		hex.addPoint(convertXfromKeyToScreenX(currentCord), convertYfromKeyToScreenY(currentCord));// south
		keyX -= 1;
		currentCord = "" + keyX + "," + keyY;
		hex.addPoint(convertXfromKeyToScreenX(currentCord), convertYfromKeyToScreenY(currentCord));// southwest
		keyY += 1;
		currentCord = "" + keyX + "," + keyY;
		hex.addPoint(convertXfromKeyToScreenX(currentCord), convertYfromKeyToScreenY(currentCord));// northwest

		return hex;
	}

	// checkt of er bij het bouwen van een road een fout is en als ja haalt hij de
	// road punten uit de array
	private void errorRoad() {
		if (beginPointIsMiddlePoint && !endPointIsMiddlePoint) {
			roadEndPoints.remove(roadEndPoints.size() - 1);
			beginPointIsMiddlePoint = false;
		} else if (endPointIsMiddlePoint && !beginPointIsMiddlePoint) {
			roadBeginPoints.remove(roadBeginPoints.size() - 1);
			endPointIsMiddlePoint = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	// registreerd de beginpunten van de roads
	@Override
	public void mousePressed(MouseEvent e) {
		JButton village = selfViewWithButtons.getVillageButton();
		JButton road = selfViewWithButtons.getRoadButton();

		if (dice.getTotalValue() == 7 || playDevelopmentCard) {
			Point p = new Point(e.getX(), e.getY());
			String newKey = convertXYfromScreenToKey(p.x, p.y);
			if (klikpunten.getPoints().containsValue(newKey)) {
				xKey = convertXfromKeyToScreenX(newKey);
				yKey = convertYfromKeyToScreenY(newKey);
				for (int i = 0; i < middlePointStringArray.length; i++) {
					if (newKey.equals(middlePointStringArray[i])) {
						robbert = i + 1;
						catanController.getCatangui().getCatanView().getGamesAndChatView().getChatView().getChatModel()
								.logRobber(robbert);
						catanController.getModel().updateRobberLocationInDB(robbert);

						CatanModel catanmodel = catanController.getModel();
						Tile[] tiles = boardController.getTiles();
						Tile t = tiles[catanmodel.getRobberLocationFromDB() - 1];
						Location[] l = t.getBorderLoc();
						for (int a = 0; a < l.length; a++) {
							if (l[a].hasSettlement()) {
								int g = catanmodel.getUser(l[a].getSettlementUser()).getNumber();
								System.out.println("in de ifstatement van docard");
								catanController.stealCard(g - 1);
								break;

							}
						}

						updateRobbert();
						playDevelopmentCard = false;
						repaint();
						removeMouseListener(this);
					}
				}
			}
		}

		if (buildOption == 1 || buildOption == 4) {
			endPointIsMiddlePoint = false;
			beginPointIsMiddlePoint = false;
			String currentPointPressed = klikpunten.getKeyWithoutMP(e.getX(), e.getY());
			try {
				Point p = new Point(convertXfromKeyToScreenX(currentPointPressed),
						convertYfromKeyToScreenY(currentPointPressed));

				if (klikpunten.getPointsWithoutMP().containsKey(p)) {
					roadBeginPoints.add(p);
				}
			} catch (NullPointerException e1) {
				beginPointIsMiddlePoint = true;
			}
		} else if (buildOption == 2 || buildOption == 5) {
			try {
				String currentPoint = klikpunten.getKeyWithoutMP(e.getX(), e.getY());
				Point p = new Point();
				p.setLocation(convertXfromKeyToScreenX(currentPoint), convertYfromKeyToScreenY(currentPoint));

				if (klikpunten.getPointsWithoutMP().containsKey(p)) {
					villagePoints.add(p);
				}
			} catch (NullPointerException e1) {
				e1.printStackTrace();
				return;
			}

			String one = convertXYfromScreenToKey(villagePoints.get(villagePoints.size() - 1).x,
					villagePoints.get(villagePoints.size() - 1).y);
			String[] c = one.split(",");
			int xvOne = Integer.parseInt(c[0]);
			int yvOne = Integer.parseInt(c[1]);

			if (buildOption == 2 && catanController.theVillageBuilder(xvOne, yvOne, true)) {
				repaint();
				setBuildOption(0);

				catanController.getModel().getOwnUser().addVillage(xvOne, yvOne);
				catanController.getCatangui().getCatanView().getGamesAndChatView().getChatView().getChatModel()
						.logVillage();

				removeMouseListener(this);
			} else if (buildOption == 5 && catanController.theVillageBuilder(xvOne, yvOne, false)) {
				repaint();
				setBuildOption(0);
				System.out.println("wtf doe je hier boardview");
				village.setEnabled(false);
				road.setEnabled(true);

				catanController.getModel().getOwnUser().addVillage(xvOne, yvOne);
				catanController.getCatangui().getCatanView().getGamesAndChatView().getChatView().getChatModel()
						.logVillage();

				removeMouseListener(this);
			} else {
				villagePoints.remove(villagePoints.size() - 1);
			}
		} else if (buildOption == 3) {
			try {
				String currentPoint = klikpunten.getKeyWithoutMP(e.getX(), e.getY());
				Point p = new Point();
				p.setLocation(convertXfromKeyToScreenX(currentPoint), convertYfromKeyToScreenY(currentPoint));

				if (klikpunten.getPointsWithoutMP().containsKey(p)) {
					villagePoints.remove(p);
					cityPoints.add(p);
				}
			} catch (NullPointerException e1) {
				e1.printStackTrace();
			}

			selfViewWithButtons.setX1(0);
			selfViewWithButtons.setX2(0);
			selfViewWithButtons.setY1(0);
			selfViewWithButtons.setY2(0);

			String one = convertXYfromScreenToKey(cityPoints.get(cityPoints.size() - 1).x,
					cityPoints.get(cityPoints.size() - 1).y);

			String[] c = one.split(",");
			int xcOne = Integer.parseInt(c[0]);
			int ycOne = Integer.parseInt(c[1]);

			if (buildOption == 3 && catanController.theCityBuilder(xcOne, ycOne)) {
				villagePoints.add(cityPoints.get(cityPoints.size() - 1).getLocation());
				repaint();
				catanController.getModel().getOwnUser().addCity(xcOne, ycOne);
				catanController.getCatangui().getCatanView().getGamesAndChatView().getChatView().getChatModel()
						.logCity();

			} else {
				cityPoints.remove(cityPoints.size() - 1);
			}

			removeMouseListener(this);
		}
	}

	// registreerd de eindpunten van de raods
	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (buildOption == 1 || buildOption == 4) {
			try {
				String currentPointReleased = klikpunten.getKeyWithoutMP(arg0.getX(), arg0.getY());
				if (beginPointIsMiddlePoint) {
					beginPointIsMiddlePoint = false;
					return;
				}

				Point p = new Point();
				p.setLocation(this.convertXfromKeyToScreenX(currentPointReleased),
						this.convertYfromKeyToScreenY(currentPointReleased));
				if (!klikpunten.getPointsWithoutMP().containsKey(p)) {
					return;
				}

				roadEndPoints.add(p);

				selfViewWithButtons.setX1(roadBeginPoints.get(roadBeginPoints.size() - 1).x);
				selfViewWithButtons.setX2(roadEndPoints.get(roadEndPoints.size() - 1).x);
				selfViewWithButtons.setY1(roadBeginPoints.get(roadBeginPoints.size() - 1).y);
				selfViewWithButtons.setY2(roadEndPoints.get(roadEndPoints.size() - 1).y);

			} catch (NullPointerException e) {
				endPointIsMiddlePoint = true;
			}

			errorRoad();
			checkDistance();

			selfViewWithButtons.setX1(0);
			selfViewWithButtons.setX2(0);
			selfViewWithButtons.setY1(0);
			selfViewWithButtons.setY2(0);

			int x = roadBeginPoints.get(roadBeginPoints.size() - 1).x;
			int y = roadBeginPoints.get(roadBeginPoints.size() - 1).y;
			String one = convertXYfromScreenToKey(x, y);

			String[] c = one.split(",");
			int xOne = Integer.parseInt(c[0]);
			int yOne = Integer.parseInt(c[1]);

			String two = convertXYfromScreenToKey(roadEndPoints.get(roadEndPoints.size() - 1).x,
					roadEndPoints.get(roadEndPoints.size() - 1).y);
			String[] t = two.split(",");
			int xTwo = Integer.parseInt(t[0]);
			int yTwo = Integer.parseInt(t[1]);

			try {
				if (buildOption == 1 && !catanController.roadBuilder(xOne, yOne, xTwo, yTwo, true)) {
					roadBeginPoints.remove(roadBeginPoints.size() - 1);
					roadEndPoints.remove(roadEndPoints.size() - 1);

					return;
				} else if (buildOption == 4 && !catanController.roadBuilder(xOne, yOne, xTwo, yTwo, false)) {
					roadBeginPoints.remove(roadBeginPoints.size() - 1);
					roadEndPoints.remove(roadEndPoints.size() - 1);

					return;
				} else {
					catanController.getModel().getOwnUser().addRoad(xOne, yOne, xTwo, yTwo);
					catanController.getCatangui().getCatanView().getGamesAndChatView().getChatView().getChatModel()
							.logRoad();
					this.removeMouseListener(this);
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

			repaint();

			if (buildOption == 4) {
				// selfViewWithButtons.endFirstTurn();
				selfViewWithButtons.getEndTurnButton().setEnabled(true);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	public void setBuildOption(int buildOption) {
		this.buildOption = buildOption;
	}

	public void updateRobbert() {
		xKey = this.convertXfromKeyToScreenX(
				middlePointStringArray[catanController.getModel().getRobberLocationFromDB() - 1]);
		yKey = this.convertYfromKeyToScreenY(
				middlePointStringArray[catanController.getModel().getRobberLocationFromDB() - 1]);
	}

	private void drawRobber(Graphics g) {
		xKey = this.convertXfromKeyToScreenX(
				middlePointStringArray[catanController.getModel().getRobberLocationFromDB() - 1]);
		yKey = this.convertYfromKeyToScreenY(
				middlePointStringArray[catanController.getModel().getRobberLocationFromDB() - 1]);
		g.setColor(Color.BLACK);
		g.fillOval(xKey, yKey, 10, 10);
	}

	public void setPlayDevelopmentCard(boolean b) {
		playDevelopmentCard = b;
	}

	public int getRobbie() {
		return robbert;
	}

	public void addVillages(int userIndex, Village[] villages) {
		for (Village village : villages) {
			int x = village.getXPos1();
			int y = village.getYPos1();
			String key = x + "," + y;

			Point point = new Point(convertXfromKeyToScreenX(key), convertYfromKeyToScreenY(key));

			if (!villagePoints.contains(point) && x != 0 && y != 0) {
				villagePoints.add(point);

				addPointToUserArrayList(userIndex, point);
				boardController.getLocation(x, y).setSettlement(village);
			}
		}

		repaint();
	}

	public void addCities(int userIndex, City[] cities) {
		for (City city : cities) {
			int x = city.getXPos1();
			int y = city.getYPos1();
			String key = x + "," + y;

			Point point = new Point(convertXfromKeyToScreenX(key), convertYfromKeyToScreenY(key));

			if (!cityPoints.contains(point) && x != 0 && y != 0) {
				cityPoints.add(point);

				addPointToUserArrayList(userIndex, point);
				boardController.getLocation(x, y).getLocModel().setSettlement(city);
			}
		}

		repaint();
	}

	public void addRoads(int userIndex, Road[] roads) {
		for (Road road : roads) {
			int x1 = road.getXPos1();
			int y1 = road.getYPos1();
			String key1 = x1 + "," + y1;

			int x2 = road.getXPos2();
			int y2 = road.getYPos2();
			String key2 = x2 + "," + y2;

			if (x1 == 0 || y1 == 0 || x2 == 0 || y2 == 0) {
				return;
			}

			Point point1 = new Point(convertXfromKeyToScreenX(key1), convertYfromKeyToScreenY(key1));

			Point point2 = new Point(convertXfromKeyToScreenX(key2), convertYfromKeyToScreenY(key2));

			if (!(roadBeginPoints.contains(point1) && roadEndPoints.contains(point2))) {
				roadBeginPoints.add(point1);
				roadEndPoints.add((point2));

				addPointToUserArrayList(userIndex, point1);
				addRoadEndPointToUserArrayList(userIndex, point2);
			}
		}

		repaint();
	}

	private void addPointToUserArrayList(int userIndex, Point point) {
		if (userIndex == 0) {
			user1Points.add(point);
		} else if (userIndex == 1) {
			user2Points.add(point);
		} else if (userIndex == 2) {
			user3Points.add(point);
		} else if (userIndex == 3) {
			user4Points.add(point);
		}
	}

	private void addRoadEndPointToUserArrayList(int userIndex, Point point) {
		if (userIndex == 0) {
			user1RoadEndPoints.add(point);
		} else if (userIndex == 1) {
			user2RoadEndPoints.add(point);
		} else if (userIndex == 2) {
			user3RoadEndPoints.add(point);
		} else if (userIndex == 3) {
			user4RoadEndPoints.add(point);
		}
	}

}