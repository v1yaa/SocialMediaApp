package socialmedia.model;

import java.util.ArrayList;
import java.util.List;

/**
 * MODEL: Holds all data for a single User.
 * No logic here — just fields and getters/setters.
 */
public class User {

    private String username;
    private String name;
    private String dateOfBirth;   // format: DD/MM/YYYY
    private int    age;
    private String location;
    private String occupation;

    private List<String> friends;           // usernames of accepted friends
    private List<String> sentRequests;      // usernames this user sent requests to
    private List<String> receivedRequests;  // usernames who sent requests to this user
    private List<String> notifications;

    // ── Constructor ──────────────────────────────────────
    public User(String username, String name, String dateOfBirth,
                int age, String location, String occupation) {
        this.username        = username;
        this.name            = name;
        this.dateOfBirth     = dateOfBirth;
        this.age             = age;
        this.location        = location;
        this.occupation      = occupation;

        this.friends          = new ArrayList<>();
        this.sentRequests     = new ArrayList<>();
        this.receivedRequests = new ArrayList<>();
        this.notifications    = new ArrayList<>();
    }

    // ── Getters ──────────────────────────────────────────
    public String getUsername()    { return username; }
    public String getName()        { return name; }
    public String getDateOfBirth() { return dateOfBirth; }
    public int    getAge()         { return age; }
    public String getLocation()    { return location; }
    public String getOccupation()  { return occupation; }

    public List<String> getFriends()          { return friends; }
    public List<String> getSentRequests()     { return sentRequests; }
    public List<String> getReceivedRequests() { return receivedRequests; }
    public List<String> getNotifications()    { return notifications; }

    // ── Helper methods ───────────────────────────────────
    public boolean isFriend(String username) {
        return friends.contains(username);
    }

    public void addFriend(String username) {
        if (!friends.contains(username)) {
            friends.add(username);
        }
    }

    public void addNotification(String message) {
        notifications.add(message);
    }

    public void clearNotifications() {
        notifications.clear();
    }

    // ── DOB month helper used for friend suggestions ─────
    public String getDobMonth() {
        if (dateOfBirth != null && dateOfBirth.length() >= 5) {
            return dateOfBirth.substring(3, 5);
        }
        return "";
    }

    @Override
    public String toString() {
        return String.format(
            "  Name       : %s%n  Age        : %d%n  Location   : %s%n  Occupation : %s",
            name, age, location, occupation);
    }
}
