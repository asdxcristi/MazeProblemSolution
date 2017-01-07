import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author Ionut-Cristian Bucur, 323CA Implementare a algoritmului Lee modificat
 *         pentru scopurile temei
 */

public class Lee {
	static final String FILE_OUT = "maze.out";
	/*
	 * Container folosit pentru a tine datele necesare algoritmului
	 */
	private Node[][] matrix;
	/*
	 * Obiectul din care extragem datele necesare algoritmului
	 */
	private CityMap cityMap;
	/*
	 * Characterul(punctul) de care vrem sa plecam si la care vrem sa ajungem
	 */
	private Character start;
	private Character stop;
	/*
	 * SD folosit in implementarea algoritmului
	 */
	private ArrayList<Node> open;

	/*
	 * Clasa ce contine coordonatele unui punct si metode ce tin de acestea
	 * 
	 */
	class Dot {
		private int x;
		private int y;

		public Dot(int x, int y) {
			setCoordinates(x, y);
		}

		private void setCoordinates(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getXCoord() {

			return x;
		}

		public int getYCoord() {

			return y;
		}

		/*
		 * Metoda ce pentru aflat vecinul unui punct din harta
		 * 
		 * @param neigh pozitia(UP,DOWN,DIAG_LEFT_UP etc)
		 * 
		 * @param xLength-1 coordonata maxima de pe axa X
		 * 
		 * @param yLength-1 coordonata maxima de pe axa Y
		 * 
		 * @return un obiect Dot cu coordonate(-1,-1) daca nu exista vecinul
		 * cerut sau un obiect Dot cu coordonate tipului de vecin cerut
		 */
		private Dot getNeigh(Neigh neigh, int xLength, int yLength) {
			switch (neigh) {

			case DIAG_LEFT_UP:
				if (y - 1 >= 0 && x - 1 >= 0) {
					return new Dot(x - 1, y - 1);
				}
				break;

			case UP:
				if (y - 1 >= 0 && x >= 0) {
					return new Dot(x, y - 1);
				}
				break;

			case DIAG_RIGHT_UP:
				if (y - 1 >= 0 && x + 1 < xLength) {
					return new Dot(x + 1, y - 1);
				}
				break;

			case LEFT:
				if (y >= 0 && x - 1 >= 0) {
					return new Dot(x - 1, y);
				}
				break;

			case RIGHT:
				if (y >= 0 && x + 1 < xLength) {
					return new Dot(x + 1, y);
				}
				break;

			case DIAG_LEFT_DOWN:
				if (y + 1 < yLength && x - 1 >= 0) {
					return new Dot(x - 1, y + 1);
				}
				break;

			case DOWN:
				if (y + 1 < yLength && x >= 0) {
					return new Dot(x, y + 1);
				}
				break;

			case DIAG_RIGHT_DOWN:
				if (y + 1 < yLength && x + 1 < xLength) {
					return new Dot(x + 1, y + 1);
				}
				break;

			default:
				return new Dot(-1, -1);
			}

			return new Dot(-1, -1);
		}

		/*
		 * Metoda ce pentru scos toti vecinii unui punct din harta se bazeaza pe
		 * metoda getNeigh pentru a forma un vector cu toti vecinii
		 * 
		 * 
		 * @param xLength-1 coordonata maxima de pe axa X
		 * 
		 * @param yLength-1 coordonata maxima de pe axa Y
		 * 
		 * @return un vector de tip Dot[8] cu toti vecinii punctului cerut
		 */
		public Dot[] getAllNeigh(int xLength, int yLength) {

			Dot[] allNeigh = new Dot[8];

			allNeigh[0] = getNeigh(Neigh.DIAG_LEFT_UP, xLength, yLength);
			allNeigh[1] = getNeigh(Neigh.UP, xLength, yLength);
			allNeigh[2] = getNeigh(Neigh.DIAG_RIGHT_UP, xLength, yLength);
			allNeigh[3] = getNeigh(Neigh.LEFT, xLength, yLength);
			allNeigh[4] = getNeigh(Neigh.RIGHT, xLength, yLength);
			allNeigh[5] = getNeigh(Neigh.DIAG_LEFT_DOWN, xLength, yLength);
			allNeigh[6] = getNeigh(Neigh.DOWN, xLength, yLength);
			allNeigh[7] = getNeigh(Neigh.DIAG_RIGHT_DOWN, xLength, yLength);

			return allNeigh;
		}

	}

	/*
	 * Clasa ce se bazeaza pe clasa Dot,dar contine si valoarea din acel punct
	 * folosita in ArrayListul pe care se bazeaza algoritmul Lee
	 */
	class Node extends Dot {

		private int value;

		public Node(int x, int y, int value) {
			super(x, y);
			setValue(value);
		}

		public void setValue(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}

	}

	/**
	 * Extrage datele si le copiaza in containerele din aceasta clasa,pentru a
	 * le putea modifica fara a afecta datele initiale in caz ca mai este nevoie
	 * de ele ulterior
	 * 
	 * @param cityMap
	 *            obiectul din care extragem date precum caracterele si
	 *            harta(containerlui matrice0
	 */
	public Lee(CityMap cityMap) {

		open = new ArrayList<Node>();

		this.matrix = new Node[cityMap.getMapYLength()][cityMap
				.getMapXLength()]; // initializam matricea container cu aceleasi
									// marimi cu alea celei pe care o extragem

		// extragem caracter
		start = cityMap.getCharacters()[0];
		stop = cityMap.getCharacters()[1];

		// incarcam efectiv elementele din matrice in containerul curent
		loadMatrix(cityMap);

	}

	/*
	 * Incarca elementele dintr-un obiect de tip cityMap in matricea container
	 * din aceasta clasa
	 * 
	 * @param cityMap obiectul din care extragem date precum caracterele si
	 * harta(containerlui matrice0
	 */
	private void loadMatrix(CityMap cityMap) {

		this.cityMap = cityMap;

		char[][] temp = cityMap.getMatrixCotainer();

		for (int i = 0; i < cityMap.getMapYLength(); i++) {

			for (int j = 0; j < cityMap.getMapXLength(); j++) {

				if (temp[i][j] == ' ' || temp[i][j] == start.getName()
						|| temp[i][j] == stop.getName()) {
					matrix[i][j] = new Node(j, i, 0);

				} else {

					matrix[i][j] = new Node(j, i, -1);
				}
			}
		}
	}

	/*
	 * Aplica algoritmul lui Lee pentru a aflat distana minima dintre 2
	 * puncte(charactere)
	 * 
	 * @param start Characterul(pozitia) de un plecam
	 * 
	 * @param stop Characterul(pozitia) unde vrem sa ajungem
	 * 
	 * @return matricea "Lee-ficata"
	 */
	private Node[][] Start(Character start, Character stop) {

		Node[][] matrix1 = new Node[cityMap.getMapYLength()][cityMap
				.getMapXLength()]; // initializam o noua matrice pentru a nu o
									// strica pe cea din containerul curent

		// copiem toate elementele din containerul curent in matricea noua
		for (int i = 0; i < cityMap.getMapYLength(); i++)
			for (int j = 0; j < cityMap.getMapXLength(); j++)
				matrix1[i][j] = matrix[i][j];

		// punem valoarea 1 in pozitia de start
		matrix[start.getY()][start.getX()].setValue(1);

		// adaugam pozitia de start in ArrayList
		open.add(matrix1[start.getY()][start.getX()]);

		while (!open.isEmpty()) {

			Node temp = open.remove(0);// scoatem primul element din ArrayList

			int xLength = cityMap.getMapXLength();

			int yLength = cityMap.getMapYLength();

			// scoatem toti vecinii nodului curent
			Dot[] neigh = temp.getAllNeigh(xLength, yLength);

			for (int i = 0; i < 8; i++) {

				// scoatem coordonatele nodului curent
				int x = neigh[i].getXCoord();
				int y = neigh[i].getYCoord();

				if (x == -1 && y == -1) {// in caz ca nu are vecinul
											// cerut,mergem mai departe
					continue;
				} else {

					if (matrix1[y][x].getValue() == 0) { // daca elementul este
															// un spatiu prin
															// care se poate
															// trece

						// adaugam numarul pasului curent
						matrix1[y][x].setValue(
								matrix1[temp.getYCoord()][temp.getXCoord()]
										.getValue() + 1);

						// adaugam elementul in capatul ArrayListului
						open.add(matrix1[y][x]);

					}

				}

			}
		}
		return matrix1;

	}

	/*
	 * Calculeaza timpul intalnirii celor 2 charactere
	 */
	private int getTimeToFindThePath() {

		int time = getPathLength();

		return time / 2 + 1;
	}

	/*
	 * Calculeaza lungimea drumul intalnirii celor 2 charactere aceasta se va
	 * afla in valorea elementul aflat in pozitia de stop(unde vrem sa ajungem)
	 */
	private int getPathLength() {

		int endX = stop.getX(), endY = stop.getY();

		Node stop = matrix[endY][endX];

		int pathLength = stop.getValue();

		return pathLength;
	}

	/**
	 * Gaseste punctele de intalnire conform timpului minim Porneste algoritmul
	 * de la Romeo la Julieta continua cu de la Julieta la Romeo dupa care
	 * gaseste punctele de intalnire cautand in ambele matrici locurile unde se
	 * afla valoarea timpul de intalnire in aceleasi coordonate in ambele
	 * matrici
	 * 
	 * 
	 * scrie in fisierul FILE_OUT sub forma: timp_intalnire y_intalnire
	 * x_intalnire
	 */
	public void Path() {

		Node[][] firstMatrix = Start(start, stop); // aplicam algoritmul de la
													// Romeo la Julieta

		if (matrix[stop.getY()][stop.getX()].getValue() == 0) {// daca nu exista
																// drum la cei
																// doi
			try {
				PrintWriter writer = new PrintWriter(
						new FileOutputStream(new File(FILE_OUT), true));
				writer.println("INF"); // scriem in fisier mesajul corespunzator
				writer.close();
			} catch (Exception e) {
				System.out.println("A crapat la scrierea in fisier");

			}
		}

		int time = getTimeToFindThePath();

		loadMatrix(cityMap); // reincarcam containerul matrice pentru a putea
								// aplica din nou algoritmul

		Node[][] stopMatrix = Start(stop, start); // aplicam algoritmul de la
													// Julieta la Romeo

		for (int i = 0; i < cityMap.getMapYLength(); i++) {// parcurgem ambele
															// matrici
			for (int j = 0; j < cityMap.getMapXLength(); j++) {

				// verificam daca am gasit locul de intalnire
				if (firstMatrix[i][j].getValue() == stopMatrix[i][j].getValue()
						&& firstMatrix[i][j].getValue() == time) {

					try {
						PrintWriter writer = new PrintWriter(
								new FileOutputStream(new File(FILE_OUT), true));

						// scriem in fisier cele dorite
						writer.println(time + " " + (i + 1) + " " + (j + 1));

						writer.close();
					} catch (Exception e) {

						System.out.println("A crapat la scrierea in fisier");

					}
				}
			}
		}

	}
}
