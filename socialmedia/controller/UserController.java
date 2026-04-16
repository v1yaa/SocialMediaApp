package socialmedia.controller;
 
import socialmedia.model.User;
 
import java.util.*;
 
/**
 * CONTROLLER: All business logic related to Users.
 * Manages the user registry, friendships, and friend suggestions.
 */
public class UserController {
 
    // Central registry: username -> User object
    private Map<String, User> users = new HashMap<>();
 
    // ── Signup ───────────────────────────────────────────
    public boolean signup(String username, String name, String dob,
                          int age, String location, String occupation) {
        if (users.containsKey(username)) {
            System.out.println("  [!] Username already taken. Try another.");
            return false;
        }
        users.put(username, new User(username, name, dob, age, location, occupation));
        System.out.println("  [✓] Account created for " + name + "!");
        return true;
    }
 
    // ── Lookup ───────────────────────────────────────────
    public User getUser(String username) {
        return users.get(username);
    }
 
    public boolean userExists(String username) {
        return users.containsKey(username);
    }
 
    public Collection<User> getAllUsers() {
        return users.values();
    }
 
    // ── View friends list ─────────────────────────────────
    public void viewFriends(User me) {
        if (me.getFriends().isEmpty()) {
            System.out.println("  You have no friends yet. Send some friend requests!");
            return;
        }
        System.out.println("  Your Friends (" + me.getFriends().size() + "):");
        printDivider();
        for (String friendUsername : me.getFriends()) {
            User f = getUser(friendUsername);
            if (f != null) {
                System.out.println(f);
                printDivider();
            }
        }
    }
 
    // ─────────────────────────────────────────────────────
    //  FRIEND SUGGESTIONS  — 3-tier ranking system
    //  Tier 1: mutual friends (sorted by mutual count)
    //  Tier 2: matching profiles (sorted by match score)
    //  Tier 3: everyone else
    // ─────────────────────────────────────────────────────
    public void showFriendSuggestions(User me) {
 
        List<User> mutualGroup   = new ArrayList<>();
        List<User> matchingGroup = new ArrayList<>();
        List<User> otherGroup    = new ArrayList<>();
 
        Map<String, Integer>       mutualCount = new HashMap<>();
        Map<String, List<String>>  mutualNames = new HashMap<>();
        Map<String, Integer>       matchScore  = new HashMap<>();
 
        for (User other : users.values()) {
            String ou = other.getUsername();
 
            // Skip self, existing friends, and already-sent requests
            if (ou.equals(me.getUsername()))                   continue;
            if (me.isFriend(ou))                               continue;
            if (me.getSentRequests().contains(ou))             continue;
 
            // --- Count mutual friends ---
            int mutual = 0;
            List<String> sharedNames = new ArrayList<>();
            for (String myFriend : me.getFriends()) {
                if (other.isFriend(myFriend)) {
                    mutual++;
                    User mf = getUser(myFriend);
                    if (mf != null) sharedNames.add(mf.getName());
                }
            }
            mutualCount.put(ou, mutual);
            mutualNames.put(ou, sharedNames);
 
            // --- Compute profile match score (max 4) ---
            int score = 0;
            if (me.getName().equalsIgnoreCase(other.getName()))           score++;
            if (me.getAge() == other.getAge())                            score++;
            if (!me.getDobMonth().isEmpty()
                    && me.getDobMonth().equals(other.getDobMonth()))      score++;
            if (me.getLocation().equalsIgnoreCase(other.getLocation()))   score++;
            matchScore.put(ou, score);
 
            // --- Classify into tier ---
            if (mutual > 0)      mutualGroup.add(other);
            else if (score > 0)  matchingGroup.add(other);
            else                 otherGroup.add(other);
        }
 
        // Sort each tier
        mutualGroup.sort((a, b) ->
                mutualCount.getOrDefault(b.getUsername(), 0)
                - mutualCount.getOrDefault(a.getUsername(), 0));
 
        matchingGroup.sort((a, b) ->
                matchScore.getOrDefault(b.getUsername(), 0)
                - matchScore.getOrDefault(a.getUsername(), 0));
 
        // --- Print Tier 1: Mutual Friends ---
        if (!mutualGroup.isEmpty()) {
            System.out.println("\n  [ Tier 1: People You May Know — Mutual Friends ]");
            printDivider();
            for (User u : mutualGroup) {
                int mc = mutualCount.get(u.getUsername());
                System.out.println(u);
                if (mc == 1) {
                    System.out.println("  Mutual friend : " + mutualNames.get(u.getUsername()).get(0));
                } else {
                    System.out.println("  Mutual friends: " + mc);
                }
                printDivider();
            }
        }
 
        // --- Print Tier 2: Matching Profiles ---
        if (!matchingGroup.isEmpty()) {
            System.out.println("\n  [ Tier 2: Matching Profiles ]");
            printDivider();
            for (User u : matchingGroup) {
                System.out.println(u);
                System.out.println("  Match score   : " + matchScore.get(u.getUsername()) + " / 4");
                printDivider();
            }
        }
 
        // --- Print Tier 3: Other Users ---
        if (!otherGroup.isEmpty()) {
            System.out.println("\n  [ Tier 3: Other Users ]");
            printDivider();
            for (User u : otherGroup) {
                System.out.println(u);
                printDivider();
            }
        }
 
        if (mutualGroup.isEmpty() && matchingGroup.isEmpty() && otherGroup.isEmpty()) {
            System.out.println("  No suggestions right now — you may already know everyone!");
        }
    }
 
    // ── Utility ───────────────────────────────────────────
    private void printDivider() {
        System.out.println("  " + "-".repeat(46));
    }
}