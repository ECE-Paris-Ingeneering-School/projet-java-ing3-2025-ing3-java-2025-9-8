package model;

import java.sql.Date;
import java.sql.Time;

public class Session {
    private int id;
    private Date sessionDate;
    private Time sessionTime;
    private String level;
    private int capacity;
    private int locationId;
    private int teacherId;
    private int dogBreedId;

    // Champs supplémentaires pour affichage
    private String teacherName;
    private String breedName;
    private String locationName;

    // Constructeur principal (pour la DAO)
    public Session(int id, Date sessionDate, Time sessionTime,
                   String level, int capacity,
                   int locationId, int teacherId, int dogBreedId) {
        this.id = id;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.level = level;
        this.capacity = capacity;
        this.locationId = locationId;
        this.teacherId = teacherId;
        this.dogBreedId = dogBreedId;
    }

    // Constructeur simplifié (selon vos besoins)
    public Session(int id, Date sessionDate, Time sessionTime,
                   String level, int capacity) {
        this(id, sessionDate, sessionTime, level, capacity, 0, 0, 0);
    }

    // Getters / Setters
    public int getId() { return id; }
    public Date getSessionDate() { return sessionDate; }
    public Time getSessionTime() { return sessionTime; }
    public String getLevel() { return level; }
    public int getCapacity() { return capacity; }
    public int getLocationId() { return locationId; }
    public int getTeacherId() { return teacherId; }
    public int getDogBreedId() { return dogBreedId; }

    public void setId(int id) { this.id = id; }
    public void setSessionDate(Date sessionDate) { this.sessionDate = sessionDate; }
    public void setSessionTime(Time sessionTime) { this.sessionTime = sessionTime; }
    public void setLevel(String level) { this.level = level; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setLocationId(int locationId) { this.locationId = locationId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
    public void setDogBreedId(int dogBreedId) { this.dogBreedId = dogBreedId; }

    // Champs pour affichage
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public String getBreedName() { return breedName; }
    public void setBreedName(String breedName) { this.breedName = breedName; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }
}
