package model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class SessionReservation {
    private int reservationId;
    private Timestamp reservationDate;
    private Date sessionDate;
    private Time sessionTime;
    private String level;
    private String locationName;

    public SessionReservation(int reservationId, Timestamp reservationDate,
                              Date sessionDate, Time sessionTime,
                              String level, String locationName) {
        this.reservationId = reservationId;
        this.reservationDate = reservationDate;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.level = level;
        this.locationName = locationName;
    }

    public int getReservationId() { return reservationId; }
    public Timestamp getReservationDate() { return reservationDate; }
    public Date getSessionDate() { return sessionDate; }
    public Time getSessionTime() { return sessionTime; }
    public String getLevel() { return level; }
    public String getLocationName() { return locationName; }
}
