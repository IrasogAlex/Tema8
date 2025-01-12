package Exercitiul1;

import java.sql.*;
import java.util.Scanner;

public class MainApp {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Meniu:");
            System.out.println("1. Adaugă persoană");
            System.out.println("2. Adaugă excursie");
            System.out.println("3. Afișează toate persoanele și excursiile");
            System.out.println("4. Afișează excursiile unei persoane");
            System.out.println("5. Afișează persoanele care au vizitat o destinație");
            System.out.println("6. Afișează persoanele care au făcut excursii într-un an");
            System.out.println("7. Șterge excursie");
            System.out.println("8. Șterge persoană și excursiile asociate");
            System.out.println("0. Ieșire");

            int optiune = scanner.nextInt();
            scanner.nextLine(); // Consumă linia rămasă
            try {
                switch (optiune) {
                    case 1:
                        adaugaPersoana();
                        break;
                    case 2:
                        adaugaExcursie();
                        break;
                    case 3:
                        afiseazaPersoaneSiExcursii();
                        break;
                    case 4:
                        afiseazaExcursiiPersoana();
                        break;
                    case 5:
                        afiseazaPersoaneDupaDestinatie();
                        break;
                    case 6:
                        afiseazaPersoaneDupaAnExcursie();
                        break;
                    case 7:
                        stergeExcursie();
                        break;
                    case 8:
                        stergePersoana();
                        break;
                    case 0:
                        System.out.println("Ieșire...");
                        return;
                    default:
                        System.out.println("Opțiune invalidă");
                }
            } catch (Exception e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }
    }

    // Adăugare persoană
    public static void adaugaPersoana() throws ExceptieVarsta {
        System.out.print("Introdu numele persoanei: ");
        String nume = scanner.nextLine();

        System.out.print("Introdu varsta persoanei: ");
        int varsta = scanner.nextInt();
        scanner.nextLine(); // Consumă linia rămasă

        // Validarea vârstei
        if (varsta < 0 || varsta > 120) {
            throw new ExceptieVarsta("Vârsta introdusă nu este validă.");
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO persoane (nume, varsta) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nume);
                stmt.setInt(2, varsta);
                stmt.executeUpdate();
                System.out.println("Persoana a fost adăugată.");
            }
        } catch (SQLException e) {
            System.out.println("Eroare la inserarea persoanei: " + e.getMessage());
        }
    }

    // Adăugare excursie
    public static void adaugaExcursie() throws SQLException, ExceptieAnExcursie {
        System.out.print("Introdu ID-ul persoanei: ");
        int idPersoana = scanner.nextInt();
        scanner.nextLine(); // Consumă linia rămasă

        System.out.print("Introdu destinația excursiei: ");
        String destinatia = scanner.nextLine();

        System.out.print("Introdu anul excursiei: ");
        int anul = scanner.nextInt();
        scanner.nextLine(); // Consumă linia rămasă

        // Validarea anului excursiei
        int anulCurent = java.time.LocalDate.now().getYear();
        if (anul < 1900 || anul > anulCurent) {
            throw new ExceptieAnExcursie("Anul excursiei nu este valid.");
        }

        // Verificăm dacă persoana există
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id FROM persoane WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idPersoana);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    System.out.println("Persoana nu există în baza de date.");
                    return;
                }
            }

            // Adăugăm excursia
            sql = "INSERT INTO excursii (id_persoana, destinatia, anul) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idPersoana);
                stmt.setString(2, destinatia);
                stmt.setInt(3, anul);
                stmt.executeUpdate();
                System.out.println("Excursia a fost adăugată.");
            }
        }
    }

    // Afișează toate persoanele și excursiile lor
    public static void afiseazaPersoaneSiExcursii() throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT p.nume, p.varsta, e.destinatia, e.anul FROM persoane p " +
                    "JOIN excursii e ON p.id = e.id_persoana";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    System.out.println("Persoana: " + rs.getString("nume") + ", Vârsta: " + rs.getInt("varsta") +
                            ", Destinație: " + rs.getString("destinatia") + ", Anul: " + rs.getInt("anul"));
                }
            }
        }
    }

    // Afișează excursiile unei persoane
    public static void afiseazaExcursiiPersoana() throws SQLException {
        System.out.print("Introdu numele persoanei: ");
        String nume = scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT e.destinatia, e.anul FROM excursii e " +
                    "JOIN persoane p ON e.id_persoana = p.id WHERE p.nume = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nume);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    System.out.println("Destinație: " + rs.getString("destinatia") + ", Anul: " + rs.getInt("anul"));
                }
            }
        }
    }

    // Afișează persoanele care au vizitat o anumită destinație
    public static void afiseazaPersoaneDupaDestinatie() throws SQLException {
        System.out.print("Introdu destinația: ");
        String destinatia = scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT p.nume FROM excursii e " +
                    "JOIN persoane p ON e.id_persoana = p.id WHERE e.destinatia = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, destinatia);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    System.out.println("Persoana: " + rs.getString("nume"));
                }
            }
        }
    }

    // Afișează persoanele care au făcut excursii într-un anumit an
    public static void afiseazaPersoaneDupaAnExcursie() throws SQLException {
        System.out.print("Introdu anul excursiei: ");
        int anul = scanner.nextInt();
        scanner.nextLine(); // Consumă linia rămasă

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT p.nume FROM excursii e " +
                    "JOIN persoane p ON e.id_persoana = p.id WHERE e.anul = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, anul);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    System.out.println("Persoana: " + rs.getString("nume"));
                }
            }
        }
    }

    // Șterge excursie
    public static void stergeExcursie() throws SQLException {
        System.out.print("Introdu ID-ul excursiei: ");
        int idExcursie = scanner.nextInt();
        scanner.nextLine(); // Consumă linia rămasă

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM excursii WHERE id_excursie = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idExcursie);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Excursia a fost ștearsă.");
                } else {
                    System.out.println("Excursia nu a fost găsită.");
                }
            }
        }
    }

    // Șterge persoană și excursiile asociate
    public static void stergePersoana() throws SQLException {
        System.out.print("Introdu ID-ul persoanei: ");
        int idPersoana = scanner.nextInt();
        scanner.nextLine(); // Consumă linia rămasă

        try (Connection conn = DBConnection.getConnection()) {
            // Ștergem excursiile persoanei
            String sql = "DELETE FROM excursii WHERE id_persoana = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idPersoana);
                stmt.executeUpdate();
            }

            // Ștergem persoana
            sql = "DELETE FROM persoane WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idPersoana);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Persoana și excursiile sale au fost șterse.");
                } else {
                    System.out.println("Persoana nu a fost găsită.");
                }
            }
        }
    }
}
