package model;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String role; // "client" ou "admin"

    public User(int id, String username, String password, String email, String name, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    // Constructeur pour inscription
    public User(String username, String password, String email, String name, String role) {
        this(0, username, password, email, name, role);
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getRole() { return role; }
}
