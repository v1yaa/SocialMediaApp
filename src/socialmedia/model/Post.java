package socialmedia.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MODEL: Holds all data for a single Post.
 * No logic here — just fields and getters/setters.
 */
public class Post {

    private static int idCounter = 1;   // auto-increment ID

    private int          id;
    private String       authorUsername;
    private String       content;
    private int          likes;
    private List<String> comments;      // stored as "Name: comment text"
    private String       timestamp;

    // ── Constructor ──────────────────────────────────────
    public Post(String authorUsername, String content) {
        this.id             = idCounter++;
        this.authorUsername = authorUsername;
        this.content        = content;
        this.likes          = 0;
        this.comments       = new ArrayList<>();
        this.timestamp      = new Date().toString();
    }

    // ── Getters ──────────────────────────────────────────
    public int          getId()             { return id; }
    public String       getAuthorUsername() { return authorUsername; }
    public String       getContent()        { return content; }
    public int          getLikes()          { return likes; }
    public List<String> getComments()       { return comments; }
    public String       getTimestamp()      { return timestamp; }

    // ── Mutators ─────────────────────────────────────────
    public void incrementLikes() {
        likes++;
    }

    public void addComment(String authorName, String text) {
        comments.add(authorName + ": " + text);
    }
}