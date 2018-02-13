package tp4.adom;
import java.io.File;

public class Main {
	
	private static final String VOISINAGE = "swap";
	private static final String INITIALISATION = "heuristique";
	private static final String MOUVEMENT = "meilleur";

	public static void main(String[] args) {
		TspParser parser = new TspParser(new File("kroA100.tsp"));
		Ville[] villes = parser.genererVilles();
		Matrice matrice = new Matrice(villes);
		
//		System.out.println(matrice);
		
		matrice.fonction_memetique("aleatoire");
		
//		Ville[] cheminHeuristique = matrice.fonction_heuristique(new Ville(1,0,0));
		
		
		//Hillclimbing
//		Ville[] cheminApresHillclimbing = matrice.fonction_hillClimbing(VOISINAGE, INITIALISATION, MOUVEMENT);
		
		//Coût après le hillclimbing
//		System.out.println(matrice.calculerCout(cheminApresHillclimbing));
	}
	
	
	
	public static void printVoisinages(Ville[][] voisinages) {
		String str = "|\t";

        for(int i=0;i<voisinages.length;i++){
            for(int j=0;j<voisinages[i].length;j++){
                str += voisinages[i][j] + "\t";
            }
            System.out.println(str + "|");
            str = "|\t";
        }

	}
}
