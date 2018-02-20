package tp4.adom;

import java.io.File;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		TspParser parser = new TspParser(new File("kroA100.tsp"));
		Ville[] villes = parser.genererVilles();
		parser = new TspParser(new File("kroB100.tsp"));
		Ville[] villes2 = parser.genererVilles();
		MTSP m = new MTSP(villes, villes2);
		int[] res = m.fonction_evalutation(m.getM1().creerCheminAleatoire());
		System.out.println(res[0] + " " + res[1]);

		// 2.2 null

		////////////

		List<Ville[]> liste = m.filtre_online(500);
		System.out.println(liste.size());
		for (Ville[] v : liste) {
			System.out.println(v);
		}

	}

}
