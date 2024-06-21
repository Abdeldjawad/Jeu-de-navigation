package Model;

import java.util.ArrayList;



public class Utilisateur {
    private String username;
    private Rank userRank;
    private ArrayList<Utilisateur> listeAmis = new ArrayList<>();
    private int meilleurScore;
    private VoitureUser voitureUser;

    public Utilisateur(String u){
        this.username = u;
        this.userRank = Rank.Bronze;
        this.meilleurScore = 0;
        this.voitureUser = new VoitureUser(225, 200, 312, 730, 230, "/images/1.png");

    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Rank getUserRank() {
        return this.userRank;
    }

    public void setUserRank(Rank userRank) {
        this.userRank = userRank;
    }

    public int getMeilleurScore() {
        return this.meilleurScore;
    }

    public void setMeilleurScore(int meilleurScore) {
        this.meilleurScore = meilleurScore;
    }

    public ArrayList<Utilisateur> getListeAmis(){
        return this.listeAmis;
    }

    public void addListeAmis(Utilisateur u){
        this.listeAmis.add(u);
    }

    public void removeListeAmis(Utilisateur u){
        this.listeAmis.remove(u);
    }


    public VoitureUser getVoitureUser() {
        return this.voitureUser;
    }
    public void setVoitureUser(VoitureUser voitureUser) {
         this.voitureUser = voitureUser;
    }

}
