package tp4.adom;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		TspParser parser = new TspParser(new File("kroA100.tsp"));
		Ville[] villes = parser.genererVilles();
		parser = new TspParser(new File("kroB100.tsp"));
		Ville[] villes2 = parser.genererVilles();
		MTSP m = new MTSP(villes, villes2);

		// 2.2 null

		////////////
		List<Chemin> ensembledesolution = new ArrayList<Chemin>(500);

		// generation de chemins aléatoires
		for (int i = 0; i < 500; i++) {
			ensembledesolution.add(new Chemin(m.getM1().creerCheminAleatoire()));
		}
		List<Chemin> liste = m.filtre_offLine(ensembledesolution);
		m.write("offlinesolutions.txt", liste);

	}

}
