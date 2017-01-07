/**
 * <h1>Tema1
 * 
 * @author Ionut-Cristian Bucur, 323CA
 */
public class Game {

	private CityMap map;

	public Game() {
		map = new CityMap();

	}

	public static void main(String[] args) {

		Game newGame = new Game(); // initializam un joc nou

		Lee pathMagic = new Lee(newGame.map); // initializam un obiect Lee

		pathMagic.Path(); // scoatem timpul si punctele de intalnire

	}

}
