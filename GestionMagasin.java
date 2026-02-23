import java.io.*;
import java.util.Scanner;

public class GestionMagasin{
    static final int MAX_PRODUITS=100;
    static String[] noms=new String[MAX_PRODUITS];
    static double[] prix=new double[MAX_PRODUITS];
    static int[] quantites=new int[MAX_PRODUITS];
    static int nbProduits=0;
    static Scanner sc=new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        chargerStock();
        int choix=0;
        while(choix!=7){
            afficherMenu();
            System.out.print("Choisissez une option : ");
            choix=Integer.parseInt(sc.nextLine());
            if(choix==1) ajouterProduit();
            if(choix==2) afficherListeProduits();
            if(choix==3) rechercherProduitConsole();
            if(choix==4) effectuerVente();
            if(choix==5) etatAlerte();
            if(choix==6){ sauvegarderStock(); 
                System.out.println("Données sauvegardées !"); }
            if(choix==7){ sauvegarderStock(); 
                System.out.println("Fermeture du programme. Données sauvegardées."); }
            if(choix<1 || choix>7) 
            System.out.println("Option invalide !");
            System.out.println("Appuyez sur Entrée pour continuer...");
            sc.nextLine();
        }
    }

    static void afficherMenu(){
        System.out.println("\nGESTION MAGASIN");
        System.out.println("1. Ajouter un nouveau produit");
        System.out.println("2. Afficher la liste des produits");
        System.out.println("3. Rechercher un produit (par nom)");
        System.out.println("4. Realiser une vente");
        System.out.println("5. Afficher les alertes (stock bas < 5)");
        System.out.println("6. Sauvegarder les donnees");
        System.out.println("7. Quitter");
    }

    static void ajouterProduit(){
        if(nbProduits>=MAX_PRODUITS){ System.out.println("Tableau plein, impossible d'ajouter !"); return; }
        System.out.print("Nom du produit : ");
        String nom=sc.nextLine();
        System.out.print("Prix : ");
        double prixProduit=Double.parseDouble(sc.nextLine());
        System.out.print("Quantité initiale : ");
        int qte=Integer.parseInt(sc.nextLine());
        noms[nbProduits]=nom;
        prix[nbProduits]=prixProduit;
        quantites[nbProduits]=qte;
        nbProduits++;
        System.out.println("Produit ajouté avec succès !");
    }

    static void rechercherProduitConsole(){
        System.out.print("Nom du produit à rechercher : ");
        String nom=sc.nextLine();
        int index=rechercherProduit(nom);
        if(index!=-1) System.out.println("Produit trouvé : "+noms[index]+", Prix : "+prix[index]+", Quantité : "+quantites[index]);
        else System.out.println("Produit introuvable !");
    }

    static int rechercherProduit(String nom){
        for(int i=0;i<nbProduits;i++){
            if(noms[i].equalsIgnoreCase(nom)) return i;
        }
        return -1;
    }

    static void effectuerVente(){
        System.out.print("Nom du produit à vendre : ");
        String nom=sc.nextLine();
        int index=rechercherProduit(nom);
        if(index==-1){ System.out.println("Produit introuvable !"); return; }
        System.out.print("Quantité à vendre : ");
        int qte=Integer.parseInt(sc.nextLine());
        if(qte>quantites[index]){ System.out.println("Stock insuffisant !"); return; }
        double total=prix[index]*qte;
        if(total>1000){ total*=0.9; System.out.println("Remise de 10% appliquée !"); }
        quantites[index]-=qte;
        System.out.println("\nTICKET DE CAISSE");
        System.out.println("Produit : "+noms[index]);
        System.out.println("Quantité : "+qte);
        System.out.println("Prix unitaire : "+prix[index]);
        System.out.println("Total à payer : "+total);
    }

    static void afficherListeProduits(){
        if(nbProduits==0){ System.out.println("Aucun produit en stock !"); return; }
        System.out.println("\nLISTE DES PRODUITS");
        System.out.println("NOM\tPRIX\tQTE\tVALEUR TOTAL");
        for(int i=0;i<nbProduits;i++){
            double valeur=prix[i]*quantites[i];
            System.out.println(noms[i]+"\t"+prix[i]+"\t"+quantites[i]+"\t"+valeur);
        }
    }

    static void etatAlerte(){
        System.out.println("\nPRODUITS EN STOCK BAS (<5)");
        boolean trouve=false;
        for(int i=0;i<nbProduits;i++){
            if(quantites[i]<5){ System.out.println(noms[i]+" | Quantité : "+quantites[i]); trouve=true; }
        }
        if(!trouve) System.out.println("Aucun produit en alerte.");
    }

    static void sauvegarderStock() throws IOException{
        PrintWriter pw=new PrintWriter(new FileWriter("stock.txt"));
        for(int i=0;i<nbProduits;i++){
            pw.println(noms[i]+";"+prix[i]+";"+quantites[i]);
        }
        pw.close();
    }

    static void chargerStock() throws IOException{
        File f=new File("stock.txt");
        if(!f.exists()) return;
        BufferedReader br=new BufferedReader(new FileReader(f));
        String ligne;
        while((ligne=br.readLine())!=null && nbProduits<MAX_PRODUITS){
            String[] parts=ligne.split(";");
            if(parts.length==3){
                noms[nbProduits]=parts[0];
                prix[nbProduits]=Double.parseDouble(parts[1]);
                quantites[nbProduits]=Integer.parseInt(parts[2]);
                nbProduits++;
            }
        }
        br.close();
    }
}