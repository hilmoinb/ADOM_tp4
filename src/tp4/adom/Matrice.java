package tp4.adom;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Matrice {

	private double[][] matrice;
	private final int NBVILLES;
	private Ville[] villes;

	
	public Matrice(Ville[] villes) {
		this.villes = villes;
		this.NBVILLES = villes.length;
		this.matrice = new double[NBVILLES + 1][NBVILLES + 1];
		for (int i = 1; i < NBVILLES+1; i++) {
			for (int j = i; j < NBVILLES+1; j++) {
				this.matrice[i][j] = villes[i-1].distance(villes[j-1]);
			}
		}
	}

	
	@Override
	public String toString() {
		String toReturn = "";
		for (int i = 1; i < matrice.length; i++) {
			for (int j = 1; j < matrice.length; j++)
				toReturn += matrice[i][j] + " ,";
			toReturn += "\n";
		}
		return toReturn;
	}

	
	/**
	 * Calcule le coût de parcours d'un chemin
	 * @param chemin - Le chemin dont on veut calculer le coût
	 * @return  Le coût du chemin
	 */
	public int calculerCout(Ville[] chemin) {
		int res = 0;
		// on pourrait ajouter des if qui check si la taille du chemin est bien = à
		// NBVILLES, si ce sont les bonnes villes etc
		for (int i = 0; i < NBVILLES; i++)
			res += distance(chemin[i], chemin[(i + 1) % NBVILLES]);

		return res;
	}

	
	/**
	 * Fonction générant une solution complètement aléatoire pour le problème du TSP
	 * @return Un chemin solution
	 */
	public Ville[] creerCheminAleatoire() {
		ArrayList<Ville> list = new ArrayList<>();
		for (Ville v : this.villes)
			list.add(v);

		Ville res[] = new Ville[NBVILLES];
		int cpt = 0;
		while (!list.isEmpty()) {
			int random = new Random().nextInt(list.size());
			res[cpt++] = list.get(random);
			list.remove(random);
		}
		return res;
	}
	
	
	/**
	 * Fonction générant 100 solutions complètement aléatoires pour le problème du TSP et retourne la moins coûteuse
	 * @return  Un chemin solution
	 */
	public Ville[] fonction_solutionAleatoireGlobale() {
		System.out.println("GENERATION DE SOLUTIONS ALEATOIRES :");
		Ville[] bestChemin = this.creerCheminAleatoire();
		int bestCost = this.calculerCout(bestChemin);
		System.out.println("\nChemin : " + TspParser.cheminToString(bestChemin) + "\nCoût : " + bestCost);
		
		Ville[] chemin;
		int cost;
		for(int i=1; i<=100; i++) {
			chemin = this.creerCheminAleatoire();
			cost = this.calculerCout(chemin);
			System.out.println("\nChemin : " + TspParser.cheminToString(chemin) + "\nCoût : " + this.calculerCout(chemin));
			if(cost < bestCost) {
				bestChemin = chemin;
				bestCost = cost;
			}
		}
		System.out.println("MEILLEURE SOLUTION TROUVÉE :\nChemin : " + TspParser.cheminToString(bestChemin) + "\nCoût : " + bestCost);
		return bestChemin;
	}

	
	/**
	 * Fonction générant la meilleure solution à l'aide de la méthode heuristique constructive pour le problème du TSP. On applique la fonction_heuristique avec toutes les villes de départ possibles et on garde la meilleure solution
	 * @return  Le chemin le moins coûteux en méthode heuristique constructive
	 */
	public Ville[] fonction_heuristiqueGlobale() {
		System.out.println("HEURISTIQUE CONSTRUCTIVE VOISIN LE PLUS PROCHE :");
		
		Ville[] bestChemin = this.fonction_heuristique(this.villes[0]);
		int bestCost = this.calculerCout(bestChemin);
		System.out.println("\nVille départ : " + this.villes[0] + "\nChemin : " + TspParser.cheminToString(bestChemin) + "\nCoût : " + bestCost);
		
		Ville[] chemin;
		int cost;
		for(int i=1; i<this.NBVILLES; i++) {
			chemin = this.fonction_heuristique(this.villes[i]);
			cost = this.calculerCout(chemin);
			System.out.println("\nVille départ : " + this.villes[i] + "\nChemin : " + TspParser.cheminToString(chemin) + "\nCoût : " + cost);
			if(cost < bestCost) {
				bestChemin = chemin;
				bestCost = cost;
			}
		}
		System.out.println("MEILLEURE SOLUTION TROUVÉE :\nVille départ : " + bestChemin[0] + "\nChemin : " + TspParser.cheminToString(bestChemin) + "\nCoût : " + bestCost);
		return bestChemin;
	}
	
	
	 /**
	  * Fonction générant une solution normalement peu coûteuse pour le problème du TSP. Elle applique la méthode du plus proche voisin comme ville suivante à parcourir
	  * @param debut - La ville à partir de laquelle notre solution démarre (et donc à partir de où commence la recherche du plus proche voisin))
	  * @return  Un chemin peu coûteux avec pour ville de départ ({@param debut}
	  */
	public Ville[] fonction_heuristique(Ville debut) {
		Ville[] chemin = new Ville[NBVILLES];
		List<Ville> restantes = new ArrayList<Ville>();
		for(Ville v : this.villes)
			restantes.add(v);
		int cpt = 0;
		Ville current = debut;
		chemin[cpt++] = current;
		restantes.remove(debut);
		
		while (!restantes.isEmpty()) {
			Ville tmp = findMin(current, restantes);
			chemin[cpt++] = tmp;
			current = tmp;
			restantes.remove(tmp);
		}
		return chemin;
	}

	
	/**
	 * Calcule la distance entre 2 villes
	 * @param v1 - La première ville
	 * @param v2 - La deuxième ville
	 * @return  La distance entre les 2 villes
	 */
	public double distance(Ville v1, Ville v2) {
		if (v1.pos > v2.pos)
			return matrice[v2.pos][v1.pos];
		else
			return matrice[v1.pos][v2.pos];
	}

	
	/**
	 * Trouve la ville la plus proche  d'une ville donnée en entrée
	 * @param ville - La ville de départ
	 * @param restantes - La liste de villes candidates
	 * @return  La ville la plus proche
	 */
	public Ville findMin(Ville ville, List<Ville> restantes) {
		double min = Double.MAX_VALUE;
		Ville sommetpetit = null;

		double tmp;
		for (Ville v : restantes) {
			tmp = distance(ville, v);
			if (tmp < min && tmp != 0) {
				min = tmp;
				sommetpetit = v;
			}
		}
		return sommetpetit;
	}

	
	/**
	 * Fonction appliquant un algorithme de recherche locale, elle trouve une solution de qualité en effectuant des mouvements dans les voisinages des solutions courantes
	 * @param voisinage - Choix d'une génération des voisinages avec la méthode swap ou two-opt
	 * @param initialisation - Choix d'une solution initialie générée aléatoirement ou à l'aide d'une heuristique constructive
	 * @param mouvement - Choix de la stratégie de déplacement premier voisin améliorant ou meilleur voisin améliorant
	 * @return  Un chemin au coût normalement peu élevé (varie en fonction des paramètres d'entrée)
	 */
	public Ville[] fonction_hillClimbing(String voisinage, String initialisation, String mouvement) {
		//System.out.println("RECHERCHE LOCALE HILL-CLIMBING :");
		
		///// INITIALISATION \\\\\
		Ville[] cheminInitial = null;
		if (initialisation.toLowerCase().compareTo("aleatoire") == 0)
			cheminInitial = this.creerCheminAleatoire();
		if (initialisation.toLowerCase().compareTo("heuristique") == 0)
			cheminInitial = this.fonction_heuristique(this.villes[new Random().nextInt(NBVILLES)]);
		if (cheminInitial == null) {
			System.err.println("Fonction fonction_hillClimbing(...) : paramètre initialisation incorrect (\""
					+ initialisation + "\")");
			return null;
		}

		//System.out.println("\nChemin initial : " + TspParser.cheminToString(cheminInitial) + "\nCoût : " + this.calculerCout(cheminInitial));

		///// VOISINAGE & MOUVEMENT \\\\\
		Ville[] cheminUpdated = cheminInitial.clone();
		Ville[][] voisinages = null;
		int cpt = 0;
		do {
			if(cpt != 0)
				//System.out.println("\nChemin intermédiaire("+cpt+") : " + TspParser.cheminToString(cheminInitial) + "\nCoût : " + this.calculerCout(cheminInitial));

			cheminInitial = cheminUpdated.clone();
			
			///// VOISINAGE \\\\\
			if (voisinage.toLowerCase().compareTo("swap") == 0)
				voisinages = this.fonction_swap(cheminInitial);
			if (voisinage.toLowerCase().compareTo("two-opt") == 0)
				voisinages = this.fonction_twoopt(cheminInitial);
			if (voisinages == null) {
				System.err.println(
						"Fonction fonction_hillClimbing(...) : paramètre voisinage incorrect (\"" + voisinage + "\")");
				return null;
			}

			///// MOUVEMENT \\\\\
			if (mouvement.toLowerCase().compareTo("meilleur") == 0)
				cheminUpdated = this.mouvement_meilleurVoisinAmeliorant(voisinages, cheminInitial);
			else if (mouvement.toLowerCase().compareTo("premier") == 0)
				cheminUpdated = this.mouvement_premierVoisinAmeliorant(voisinages, cheminInitial);
			else {
				System.err.println(
						"Fonction fonction_hillClimbing(...) : paramètre mouvement incorrect (\"" + mouvement + "\")");
				return null;
			}
			
		} while (this.calculerCout(cheminInitial) < this.calculerCout(cheminUpdated) && cpt++ < 30);

		//System.out.println("\nChemin final : " + TspParser.cheminToString(cheminUpdated) + "\nCoût : " + this.calculerCout(cheminUpdated));
		System.out.println(this.calculerCout(cheminUpdated));

		return cheminUpdated;
	}
	
	
	/**
	 * Génère tous les voisinages du chemin d'origine :
	 *  chaque voisinage est généré en supprimant 2 arrêtes et en reconnectant les 2 sous-tours obtenus
	 * @param villes Le chemin d'origine sur lequel on va baser nos voisinages
	 * @return la liste des voisinages générés, un tableau de chemins
	 */
	public Ville[][] fonction_twoopt(Ville[] chemin) {
		int nbSolutions = (NBVILLES * (NBVILLES - 3)) / 2 + 1;
		Ville[][] voisinages = new Ville[nbSolutions][NBVILLES];
		int z = -1;
		for (int i = 0; i < NBVILLES; i++) {
			for (int j = i + 2; j < NBVILLES; j++) {
				if (j == i || j == i - 1 || j == i + 1)
					continue;
				voisinages[++z] = chemin.clone();
				int a = i, b = j;
				Ville tmp;
				while (b > a) {
					tmp = voisinages[z][a];
					voisinages[z][a++] = voisinages[z][b];
					voisinages[z][b--] = tmp;
				}
			}
		}
		return voisinages;
	}

	
	/**
	 * Génère tous les voisinages du chemin d'origine :
	 *  chaque voisinage est généré en échangeant la position de 2 villes
	 * @param chemin Le chemin d'origine sur lequel on va baser nos voisinages
	 * @return la liste des voisinages générés, un tableau de chemins
	 */
	public Ville[][] fonction_swap(Ville[] chemin) {
		int nbSolutions = ((NBVILLES - 1) * (NBVILLES - 2)) / 2;
		Ville[][] voisinages = new Ville[nbSolutions][NBVILLES];
		int z = 0;
		for (int i = 1; i < chemin.length; i++) {
			for (int j = i + 1; j < chemin.length; j++) {
				Ville[] tmp = swap(chemin, i, j);
				for (int k = 0; k < tmp.length; k++) {
					voisinages[z][k] = tmp[k];
				}
				z++;
			}
		}
		return voisinages;
	}
	
	
	/**
	 * Trouver parmi les voisinages fournis celui qui a le coup le coût le plus bas
	 * @param voisinages - Liste des voisinages sous forme de tableau de tableaux
	 * @return Le meilleur voisinage trouvé, celui avec le coup le plus bas donc.  S
	 */
	public Ville[] mouvement_meilleurVoisinAmeliorant(Ville[][] voisinages, Ville[] cheminABattre) {
		Ville[] meilleurVoisinage = cheminABattre.clone();
		double meilleurCout = this.calculerCout(meilleurVoisinage), cout;
		for(Ville[] voisinage : voisinages)
			if (meilleurCout > (cout = this.calculerCout(voisinage)) ) {
				meilleurVoisinage = voisinage;
				meilleurCout = cout;
			}
		return meilleurVoisinage;
	}
	
	
	/**
	 * Trouver parmi les voisinages fournis le premier à présenter un coup plus bas
	 * @param voisinages - Liste des voisinages sous forme de tableau de tableaux
	 * @return Le meilleur voisinage trouvé, celui avec le coup le plus bas donc
	 */
	public Ville[] mouvement_premierVoisinAmeliorant(Ville[][] voisinages, Ville[] cheminABattre) {
		Ville[] meilleurVoisinage = cheminABattre.clone();
		double meilleurCout = this.calculerCout(meilleurVoisinage);
		for(Ville[] voisinage : voisinages)
			if ( meilleurCout > this.calculerCout(voisinage) )
				return voisinage;
		return meilleurVoisinage;
	}
	
	
	/**
	 * Fonction qui inverse les villes idx1 et idx2 dans le chemin
	 * @param chemin
	 * @param idx1
	 * @param idx2
	 * @return
	 */
	private Ville[] swap(Ville[] chemin, int idx1, int idx2) {
		Ville v1 = chemin[idx1];
		Ville v2 = chemin[idx2];
		chemin[idx1] = v2;
		chemin[idx2] = v1;
		return chemin;
	}
	
	
	/**
	 * Fonction qui inverse 2 villes sélectionnées aléatoirement dans le chemin
	 * @param chemin - Le chemin sur lequel on applique le swap
	 * @return  Le chemin sur lequel on a appliqué le swap
	 */
	public Ville[] fonction_swap_aleatoire(Ville[] chemin) {
		Random r = new Random();
		int nb1 = r.nextInt(NBVILLES);
		int nb2 = r.nextInt(NBVILLES);
		while (nb1 == nb2)
			nb2= r.nextInt(NBVILLES);
		return swap(chemin, nb1, nb2);
	}
	
	
	/**
	 * Fonction appliquant un algorithme évolutionnaire, elle trouve une solution de qualité en effectuant des croisements et mutations
	 * @param initialisation - Choix d'une solution initialie générée aléatoirement ou à l'aide d'une heuristique constructive
	 * @param tauxDeCroisement - POUR L'INSTANT NOMBRE DE FOIS OÙ ON EFFECTUE CROISEMENT & MUTATION ????
	 * @return  Un chemin au coût normalement peu élevé (varie en fonction des paramètres d'entrée)
	 */
	public Ville[] fonction_memetique(String initialisation, int tauxDeCroisement) {
		Ville[][] cheminsInitiaux = new Ville[4][];
		
		if (initialisation.toLowerCase().compareTo("aleatoire") == 0)
			for(int i=0; i<4; i++)
				cheminsInitiaux[i] = this.creerCheminAleatoire();
		
		if (initialisation.toLowerCase().compareTo("heuristique") == 0)
			for(int i=0; i<4; i++)
				cheminsInitiaux[i] = this.fonction_heuristique(this.villes[new Random().nextInt(NBVILLES)]);

		if (cheminsInitiaux[0] == null) {
			System.err.println("Fonction fonction_memetique(...) : paramètre initialisation incorrect (\""
				+ initialisation + "\")");
			return null;
		}
		
		///// PRINT LES CHEMINS INITIAUX \\\\\
//		for(int i=0; i<4; i++)
//			System.out.println("Coût chemin initial " + i + " : " + this.calculerCout(cheminsInitiaux[i]));
		
		for(int taux=tauxDeCroisement; taux>0; taux--) { //choix de taux à 60 pour le moment (différence entre taux de croisement et taux de mutation ??
			//Croisement puis mutation de nos chemins initiaux
			Ville[] enfant = this.fonction_croisement(cheminsInitiaux[new Random().nextInt(4)], cheminsInitiaux[new Random().nextInt(4)]);
			enfant = this.fonction_swap_aleatoire(enfant);
			
			//Trouver le chemin le plus coûteux pour le remplacer par le nouvel enfant
			int cheminPlusCher = 0;
			double coutPlusCher = this.calculerCout(cheminsInitiaux[cheminPlusCher]);
			int coutEnCours;
			for(int i=0; i<4; i++) {
				coutEnCours = this.calculerCout(cheminsInitiaux[i]);
				if( coutEnCours > coutPlusCher ) {
					coutPlusCher = coutEnCours;
					cheminPlusCher = i;
				}
			}
			cheminsInitiaux[cheminPlusCher] = enfant;
		}
		
		///// PRINT LES CHEMINS FINAUX \\\\\
//		for(int i=0; i<4; i++)
//			System.out.println("Chemin final " + i + " : " + this.calculerCout(cheminsInitiaux[i]));
		
		//Trouver le chemin le moins cher
		int cheminMoinsCher = 0;
		double coutMoinsCher = this.calculerCout(cheminsInitiaux[cheminMoinsCher]);
		int coutEnCours;
		for(int i=0; i<4; i++) {
			coutEnCours = this.calculerCout(cheminsInitiaux[i]);
			if( coutEnCours < coutMoinsCher ) {
				coutMoinsCher = coutEnCours;
				cheminMoinsCher = i;
			}
		}
		return cheminsInitiaux[cheminMoinsCher];
	}
	

	/**
	 * Fonction qui consiste à sélectionner un segment aléatoirement chez le premier chemin parent, et à ajouter les valeurs manquantes en suivant l’ordre des villes du deuxième chemin parent
	 * @param parent1 - Premier chemin parent
	 * @param parent2 - Deuxième chemin parent
	 * @return  Le résultat du croisement entre les deux parents
	 */
	public Ville[] fonction_croisement(Ville[] parent1, Ville[] parent2) {
		Ville[] enfant = new Ville[NBVILLES];
		int nb1 = new Random(100L).nextInt(NBVILLES);
		int nb2 = new Random(500L).nextInt(NBVILLES);
		if (nb1 > nb2) {
			int tmp = nb2;
			nb2 = nb1;
			nb1 = tmp;
		}
		
		List<Ville> save = new LinkedList<Ville>();
		//on place le sous chemin de parent1 au meme endroit dans enfant
		for(int i=nb1; i<=nb2; i++) {
			enfant[i] = parent1[i];
			save.add(parent1[i]);
		}
		
		//on prend en partant de la droite de parent2 (partie droite du sous chemin) et on met aux spots libres dans enfant en partant de la droite également
		int cpt = NBVILLES - 1;
		for(int i=NBVILLES-1; i>=0; i--) {
			if( !save.contains(parent2[cpt]) ) {
				if( i>nb2 || i<nb1 )
					enfant[i] = parent2[cpt--];
			} else {
				i++; //Permet de rester au même rang au prochain tour de boucle
				cpt--;
			}
		}
		return enfant;
	}
	
}
