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

	public List<Chemin> filtre_online(int nombre_solution) {
		List<Chemin> archive = new ArrayList<Chemin>();
		// generation de chemins aléatoires et evaluations de ceux ci dans les 2
		// martices.
		List<Chemin> ensemblesolutionevalue = new ArrayList<Chemin>(nombre_solution);
		for (int i = 0; i < nombre_solution; i++) {
			ensemblesolutionevalue.add(this.fonction_evalutation(new Chemin(this.m1.creerCheminAleatoire())));
		}
		this.write("allaleaon.txt", ensemblesolutionevalue);
		// recherche d'un dominant
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
				archive.add(ensemblesolutionevalue.get(i));
			}
			// verification si pas dominé sinon on supprime
			this.trie_archive(archive);
		}

		return archive;
	}

	public void trie_archive(List<Chemin> archive) {
		for (int i = 0; i < archive.size(); i++) {
			Chemin courant = archive.get(i);
			for (int j = 0; j < archive.size(); j++) {
				if ((courant.getCout1() > archive.get(j).getCout1())
						&& (courant.getCout2() > archive.get(j).getCout2())) {
					archive.remove(i);
					i = 0;
					j = 0;
				}
			}
		}
	}

	public void write(String nomfichier, List<Chemin> list) {
		final File fichier = new File(nomfichier);
		try {
			fichier.createNewFile();
			final FileWriter writer = new FileWriter(fichier);
			try {
				for (int i = 0; i < list.size(); i++) {
					writer.write(list.get(i).getCout1() + " " + list.get(i).getCout2() + "\n");
				}

			} finally {
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
