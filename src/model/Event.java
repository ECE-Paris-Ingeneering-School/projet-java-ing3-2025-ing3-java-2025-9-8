package model;

public class Event {
    private int sessionId;
    private String title;
    private String date;
    private String niveau;
    private String lieu;
    private int placesRestantes;

    // Champs supplémentaires : professeur et race
    private String teacherName;
    private String breedName;

    public Event(int sessionId, String title, String date,
                 String niveau, String lieu, int placesRestantes) {
        this.sessionId = sessionId;
        this.title = title;
        this.date = date;
        this.niveau = niveau;
        this.lieu = lieu;
        this.placesRestantes = placesRestantes;
    }

    public int getSessionId() { return sessionId; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getNiveau() { return niveau; }
    public String getLieu() { return lieu; }
    public int getPlacesRestantes() { return placesRestantes; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getBreedName() { return breedName; }
    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }
}
