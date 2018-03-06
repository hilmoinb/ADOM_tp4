package tp4.adom;

public class Chemin {

	private Ville[] chemin;
	private int cout1;
	private int cout2;

	public Chemin(Ville[] chemin) {
		this.chemin = chemin;
		cout1 = 0;
		cout2 = 0;

	}

	public Ville[] getChemin() {
		return chemin;
	}

	public void setChemin(Ville[] chemin) {
		this.chemin = chemin;
	}

	public int getCout1() {
		return cout1;
	}

	public void setCout1(int cout1) {
		this.cout1 = cout1;
	}

	public int getCout2() {
		return cout2;
	}

	public void setCout2(int cout2) {
		this.cout2 = cout2;
	}

}
