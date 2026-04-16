package socialmedia.controller;

import socialmedia.model.Post;
import socialmedia.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * CONTROLLER: All business logic related to Posts.
 * Handles creating posts, the news feed, liking, commenting, and sharing.
 */
public class PostController {

    private List<Post>     allPosts       = new ArrayList<>();
    private UserController userController;

    public PostController(UserController userController) {
        this.userController = userController;
    }

    // ── Create a new post ─────────────────────────────────
    public void createPost(User author, String content) {
        Post post = new Post(author.getUsername(), content);
        allPosts.add(0, post);   // newest first
        System.out.println("  [✓] Post created successfully!");
    }

    // ── News feed: posts from friends only, newest first ──
    public List<Post> getFeedPosts(User me) {
        List<Post> feed = new ArrayList<>();
        for (Post p : allPosts) {
            if (me.getFriends().contains(p.getAuthorUsername())) {
                feed.add(p);
            }
        }
        return feed;
    }

    // ── Posts by a specific user ──────────────────────────
    public List<Post> getPostsByUser(String username) {
        List<Post> result = new ArrayList<>();
        for (Post p : allPosts) {
            if (p.getAuthorUsername().equals(username)) result.add(p);
        }
        return result;
    }

    // ── Like a post ───────────────────────────────────────
    public void likePost(User liker, int postId) {
        Post post = findById(postId);
        if (post == null) { System.out.println("  [!] Post #" + postId + " not found."); return; }

        post.incrementLikes();
        System.out.println("  [✓] You liked Post #" + postId + "!");

        // Notify the author (not if liking own post)
        User author = userController.getUser(post.getAuthorUsername());
        if (author != null && !author.getUsername().equals(liker.getUsername())) {
            author.addNotification(liker.getName() + " liked your post: \""
                    + preview(post.getContent()) + "\"");
        }
    }

    // ── Comment on a post ─────────────────────────────────
    public void commentOnPost(User commenter, int postId, String commentText) {
        Post post = findById(postId);
        if (post == null) { System.out.println("  [!] Post #" + postId + " not found."); return; }

        post.addComment(commenter.getName(), commentText);
        System.out.println("  [✓] Comment added!");

        // Notify the author
        User author = userController.getUser(post.getAuthorUsername());
        if (author != null && !author.getUsername().equals(commenter.getUsername())) {
            author.addNotification(commenter.getName() + " commented on your post: \""
                    + preview(post.getContent()) + "\"");
        }
    }

    // ── Share a post with a friend ────────────────────────
    public void sharePost(User sharer, int postId, String targetUsername) {
        Post post = findById(postId);
        if (post == null) { System.out.println("  [!] Post #" + postId + " not found."); return; }

        User target = userController.getUser(targetUsername);
        if (target == null) { System.out.println("  [!] User '" + targetUsername + "' not found."); return; }

        if (!sharer.isFriend(targetUsername)) {
            System.out.println("  [!] You can only share posts with your friends.");
            return;
        }

        target.addNotification(sharer.getName() + " shared a post with you:\n"
                + "    \"" + post.getContent() + "\"\n"
                + "    (Post #" + post.getId() + " — you can like, comment, or share it from the feed!)");

        System.out.println("  [✓] Post #" + postId + " shared with " + target.getName() + "!");
    }

    // ── Interact menu: like / comment / share ─────────────
    public void interactWithPost(User me, int postId, Scanner scanner) {
        Post post = findById(postId);
        if (post == null) { System.out.println("  [!] Post #" + postId + " not found."); return; }

        User author = userController.getUser(post.getAuthorUsername());
        String authorName = (author != null) ? author.getName() : post.getAuthorUsername();

        System.out.println("\n  Post #" + post.getId() + " by " + authorName);
        System.out.println("  \"" + post.getContent() + "\"");
        System.out.println("\n  What would you like to do?");
        System.out.println("  1. Like");
        System.out.println("  2. Comment");
        System.out.println("  3. Share with a friend");
        System.out.println("  0. Back");
        System.out.print("  Choice: ");

        switch (scanner.nextLine().trim()) {
            case "1":
                likePost(me, postId);
                break;
            case "2":
                System.out.print("  Your comment: ");
                String text = scanner.nextLine().trim();
                if (!text.isEmpty()) commentOnPost(me, postId, text);
                else System.out.println("  [!] Comment cannot be empty.");
                break;
            case "3":
                System.out.print("  Enter friend's username to share with: ");
                sharePost(me, postId, scanner.nextLine().trim());
                break;
            case "0":
                break;
            default:
                System.out.println("  [!] Invalid option.");
        }
    }

    // ── Utilities ─────────────────────────────────────────
    public Post findById(int id) {
        for (Post p : allPosts) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    private String preview(String content) {
        return content.length() > 30 ? content.substring(0, 30) + "..." : content;
    }
}