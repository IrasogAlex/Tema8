package Exercitiul1;

public class Excursie {
    private int idExcursie;
    private String destinatia;
    private int anul;
    private int idPersoana;

    // Constructori, getter și setter
    public Excursie(int idPersoana, String destinatia, int anul) {
        this.idPersoana = idPersoana;
        this.destinatia = destinatia;
        this.anul = anul;
    }

    public int getIdExcursie() {
        return idExcursie;
    }

    public void setIdExcursie(int idExcursie) {
        this.idExcursie = idExcursie;
    }

    public String getDestinatia() {
        return destinatia;
    }

    public void setDestinatia(String destinatia) {
        this.destinatia = destinatia;
    }

    public int getAnul() {
        return anul;
    }

    public void setAnul(int anul) {
        this.anul = anul;
    }

    public int getIdPersoana() {
        return idPersoana;
    }

    public void setIdPersoana(int idPersoana) {
        this.idPersoana = idPersoana;
    }
}