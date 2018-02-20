package tp4.adom;

import java.util.ArrayList;
import java.util.List;

public class MTSP {

	private Matrice m1;
	private Matrice m2;

	public MTSP(Ville[] M1, Ville[] M2) {
		this.m1 = new Matrice(M1);
		this.m2 = new Matrice(M2);
	}

	/**
	 * On cacul le cout dans les deux matrices
	 * 
	 * @param solution_permutation
	 * @return
	 */
	public int[] fonction_evalutation(Ville[] solution_permutation) {
		int[] solution = new int[2];
		solution[0] = m1.calculerCout(solution_permutation);
		solution[1] = m2.calculerCout(solution_permutation);
		return solution;

	}

	/**
	 * Recherche de solution non domin� a partir d'un ensemble de solutions
	 * 
	 * @param ensembledesolution
	 * @return
	 */
	public List<Ville[]> filtre_offLine(Ville[][] ensembledesolution) {
		List<Ville[]> ensemble_non_domine = new ArrayList<Ville[]>();
		int[][] solutions = new int[ensembledesolution.length][2];
		for (int i = 0; i < ensembledesolution.length; i++) {
			solutions[i] = this.fonction_evalutation(ensembledesolution[i]);
		}
		// recherche d'un dominant
		boolean ajout;
		for (int i = 0; i < solutions.length; i++) {
			ajout = true;
			for (int j = 0; j < solutions.length; j++) {
				if (solutions[j][0] < solutions[i][0] && solutions[j][1] < solutions[i][1]) {
					ajout = false;
				}
			}
			if (ajout) {
				ensemble_non_domine.add(ensembledesolution[i]);
			}
		}
		return ensemble_non_domine;

	}

	public List<Ville[]> filtre_online(int nombre_solution) {
		List<Ville[]> archive = new ArrayList<Ville[]>();
		int[][] solutions = new int[nombre_solution][2];
		Ville[][] ensembledesolution = new Ville[nombre_solution][100];

		// generation de chemins aléatoires
		for (int i = 0; i < nombre_solution; i++) {
			ensembledesolution[i] = this.m1.creerCheminAleatoire();
		}

		// evalutation des chemins aléatoires dans les 2 matrices
		for (int i = 0; i < ensembledesolution.length; i++) {
			solutions[i] = this.fonction_evalutation(ensembledesolution[i]);
		}

		// recherche d'un dominant
		boolean ajout;
		for (int i = 0; i < solutions.length; i++) {
			ajout = true;
			for (int j = 0; j < solutions.length; j++) {
				if (solutions[j][0] < solutions[i][0] && solutions[j][1] < solutions[i][1]) {
					ajout = false;
				}
			}
			if (ajout) {
				archive.add(ensembledesolution[i]);
			}

			// verification si pas dominé sinon on supprime
			for (int j = 0; j < archive.size(); j++) {
				if (solutions[j][0] < solutions[i][0] && solutions[j][1] < solutions[i][1]) {
					archive.remove(solutions[j]);
				}
			}

		}

		return archive;
	}

	public Matrice getM1() {
		return m1;
	}

	public void setM1(Matrice m1) {
		this.m1 = m1;
	}

	public Matrice getM2() {
		return m2;
	}

	public void setM2(Matrice m2) {
		this.m2 = m2;
	}

}
