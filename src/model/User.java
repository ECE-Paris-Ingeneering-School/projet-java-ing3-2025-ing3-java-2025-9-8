package model;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String role;

    // Champs de livraison
    private String phone;
    private String street;
    private String complement;
    private String postalCode;
    private String city;
    private String region;

    // Champs de carte
    private String cardNumber;
    private String cardName;
    private String cardMonth;
    private String cardYear;
    private String cardCvv;

    // Constructeur principal (avec id)
    public User(int id, String username, String password, String email, String name, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    // Constructeur utilisé à l'inscription (sans id)
    public User(String username, String password, String email, String name, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    // Getters de base
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getRole() { return role; }

    // Setters de base
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }

    // Getters pour infos livraison
    public String getPhone() { return phone; }
    public String getStreet() { return street; }
    public String getComplement() { return complement; }
    public String getPostalCode() { return postalCode; }
    public String getCity() { return city; }
    public String getRegion() { return region; }

    // Setters pour infos livraison
    public void setPhone(String phone) { this.phone = phone; }
    public void setStreet(String street) { this.street = street; }
    public void setComplement(String complement) { this.complement = complement; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public void setCity(String city) { this.city = city; }
    public void setRegion(String region) { this.region = region; }

    // Getters pour infos carte
    public String getCardNumber() { return cardNumber; }
    public String getCardName() { return cardName; }
    public String getCardMonth() { return cardMonth; }
    public String getCardYear() { return cardYear; }
    public String getCardCvv() { return cardCvv; }

    // Setters pour infos carte
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public void setCardName(String cardName) { this.cardName = cardName; }
    public void setCardMonth(String cardMonth) { this.cardMonth = cardMonth; }
    public void setCardYear(String cardYear) { this.cardYear = cardYear; }
    public void setCardCvv(String cardCvv) { this.cardCvv = cardCvv; }
}
