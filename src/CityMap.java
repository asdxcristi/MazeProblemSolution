import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Ionut-Cristian Bucur, 323CA
 */
public class CityMap {

	private static final int CHARACTERS_NUMBER = 2;
	private static final char ROMEO = 'R';
	private static final char JULIETA = 'J';
	/**
	 * Fisierul din care citim harta
	 */
	private static final String FILE_IN = "maze.in";

	private final File file = new File(FILE_IN);

	/**
	 * Container sub forma unei matrice de caractere prin care retinem in
	 * memorie harta din fisierul FILE_IN
	 */
	char[][] matrixCotainer;
	/**
	 * Marimile matricei container
	 */
	int numberOfLines;
	int numberOfCols;

	Scanner scanner;
	/**
	 * Characterele prezente pe harta
	 */
	Character[] characters;

	public CityMap() {

		scanner = null;

		characters = new Character[CHARACTERS_NUMBER];

		numberOfLines = readMapSize()[0];
		numberOfCols = readMapSize()[1];

		matrixCotainer = new char[numberOfLines][numberOfCols];

		loadMap();
	}

	/*
	 * Citeste din fisierul FILE_IN marimile(numarul de linii si coloane)
	 * 
	 * @return un vector de 2 elemente,[0]-numarul de linii, [1]-numarul de
	 * coloane
	 */
	private int[] readMapSize() {
		int[] sizes = new int[2];

		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("A crapat la citire");
		}

		sizes[0] = scanner.nextInt();
		sizes[1] = scanner.nextInt();

		return sizes;
	}

	public int getMapXLength() {

		return numberOfCols;
	}

	public int getMapYLength() {
		return numberOfLines;
	}

	/*
	 * Citeste din fisierul FILE_IN matricea o incarca in container
	 * initializeaza cele doua charactere cu coordonatele lor
	 */
	private void loadMap() {
		String line = "";

		char element;

		scanner.nextLine();// scapam de de \n

		for (int i = 0; i < numberOfLines; i++) {
			line = scanner.nextLine();// citim linia curenta

			for (int j = 0; j < numberOfCols; j++) {
				element = line.charAt(j);// scoatem cate un caracter din linia
											// curenta
				if (element == ROMEO) {// verificam daca este caracter si
										// initilizam caracterul in cauza
					characters[0] = new Character(ROMEO, j, i);
				}
				if (element == JULIETA) {
					characters[1] = new Character(JULIETA, j, i);
				}
				matrixCotainer[i][j] = element;// introducem elementul curent in
												// matricea container

			}
		}

	}

	public Character[] getCharacters() {
		return characters;
	}

	public char[][] getMatrixCotainer() {
		return matrixCotainer;
	}
}
