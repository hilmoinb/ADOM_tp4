package tp4.adom;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		TspParser parser = new TspParser(new File("kroA100.tsp"));
		Ville[] villes = parser.genererVilles();
		parser = new TspParser(new File("kroB100.tsp"));
		Ville[] villes2 = parser.genererVilles();
		MTSP m = new MTSP(villes, villes2);

//		 List<Chemin> ensembledesolution = new ArrayList<Chemin>(500);
//		 for (int i = 0; i < 500; i++) {
//		 ensembledesolution.add(new Chemin(m.getM1().creerCheminAleatoire()));
//		 }
//		 List<Chemin> liste = m.filtre_offLine(ensembledesolution);
//		 m.write("offlinesolutions.txt", liste);
		
		
		List<Chemin> list = m.filtre_online(500);
		m.write("onlinesolutions.txt", list);

		long endTime =  System.currentTimeMillis();
		long elapsedMilliSeconds = endTime - startTime;
		double elapsedSeconds = elapsedMilliSeconds / 1000.0;
		System.out.println(elapsedSeconds);
		

	}

}
