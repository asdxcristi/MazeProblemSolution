/**
 * @author Ionut-Cristian Bucur, 323CA
 */
public class Character {
	private char name;
	/**
	 * Coordonatele caracterului in coordonate xOy x e numarul coloanei din
	 * matricea harta
	 */
	private int x;
	/**
	 * y e numarul liniei din matricea harta
	 */
	private int y;

	public Character() {
		x = -1;
		y = -1;
	}

	public Character(char nume, int x, int y) {
		this.name = nume;
		this.x = x;
		this.y = y;
	}

	public char getName() {
		return name;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
