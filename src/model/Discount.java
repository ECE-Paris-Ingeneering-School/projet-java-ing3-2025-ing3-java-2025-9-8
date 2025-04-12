package model;

public class Discount {
    private int id;
    private String name;          // Nom de l'offre
    private String description;   // Description
    private String conditions;    // Conditions (ex: "Quantité>=5")
    private String value;         // Ex: "10%" ou "1 gratuit"

    public Discount(int id, String name, String description, String conditions, String value) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.conditions = conditions;
        this.value = value;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getConditions() { return conditions; }
    public void setConditions(String conditions) { this.conditions = conditions; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
