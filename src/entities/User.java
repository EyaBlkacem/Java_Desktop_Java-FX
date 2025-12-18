package entities;

public class User {

    private int id;
    private String username;
    private String email;
    private String password;
    private String role; // <-- ajout du champ rôle

    // Constructeur avec rôle
    public User(int id, String username, String email, String password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // Setter pour le rôle
    public void setRole(String role) {
        this.role = role;
    }

    // Méthode pratique pour vérifier si l'utilisateur est admin
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }
}
