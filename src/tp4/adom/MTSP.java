package tp4.adom;

import java.io.File;
import java.io.FileWriter;
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
	public Chemin fonction_evalutation(Chemin solution_permutation) {
		solution_permutation.setCout1(m1.calculerCout(solution_permutation.getChemin()));
		solution_permutation.setCout2(m2.calculerCout(solution_permutation.getChemin()));
		return solution_permutation;

	}

	/**
	 * Recherche de solution non domin� a partir d'un ensemble de solutions
	 * 
	 * @param ensembledesolution
	 * @return
	 */
	public List<Chemin> filtre_offLine(List<Chemin> ensembledesolution) {
		List<Chemin> ensemble_non_domine = new ArrayList<Chemin>();
		List<Chemin> ensemblesolutionevalue = new ArrayList<Chemin>(ensembledesolution.size());
		for (int i = 0; i < ensembledesolution.size(); i++) {
			ensemblesolutionevalue.add(fonction_evalutation(ensembledesolution.get(i)));
		}
		this.write("offline_allsolutions.txt", ensemblesolutionevalue);
		boolean ajout;
		for (int i = 0; i < ensemblesolutionevalue.size(); i++) {
			ajout = true;
			for (int j = 0; j < ensemblesolutionevalue.size(); j++) {
				if (ensemblesolutionevalue.get(j).getCout1() < ensemblesolutionevalue.get(i).getCout1()
						&& ensemblesolutionevalue.get(j).getCout2() < ensemblesolutionevalue.get(i).getCout2()) {
					ajout = false;
				}
			}
			if (ajout) {
				ensemble_non_domine.add(ensemblesolutionevalue.get(i));
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

	public void write(String nomfichier, List<Chemin> list) {
		final File fichier = new File(nomfichier);
		try {
			fichier.createNewFile();
			final FileWriter writer = new FileWriter(fichier);
			try {
				for(int i = 0 ; i< list.size(); i++) {
					writer.write(list.get(i).getCout1() +" " + list.get(i).getCout2()+"\n");
				}
			
			} finally {
				writer.close();
			}
		} catch (Exception e) {
			System.out.println("Impossible de creer le fichier");
		}
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
