package socialmedia.view;

import socialmedia.controller.PostController;
import socialmedia.controller.UserController;
import socialmedia.model.Post;
import socialmedia.model.User;

import java.util.List;

/**
 * VIEW: Responsible ONLY for displaying posts and the news feed.
 * Calls PostController to get data, then formats it for the console.
 */
public class FeedView {

    private PostController postController;
    private UserController userController;

    public FeedView(PostController postController, UserController userController) {
        this.postController = postController;
        this.userController = userController;
    }

    // ── Display News Feed ─────────────────────────────────
    public void displayNewsFeed(User me) {
        List<Post> feed = postController.getFeedPosts(me);

        if (feed.isEmpty()) {
            System.out.println("  Your feed is empty. Add more friends to see their posts!");
            return;
        }

        System.out.println("  News Feed — " + feed.size() + " post(s)");
        System.out.println("  " + "-".repeat(46));

        int index = 1;
        for (Post post : feed) {
            displayPost(index, post);
            index++;
        }
    }

    // ── Display My Posts ──────────────────────────────────
    public void displayMyPosts(User me) {
        List<Post> myPosts = postController.getPostsByUser(me.getUsername());

        if (myPosts.isEmpty()) {
            System.out.println("  You haven't posted anything yet.");
            return;
        }

        System.out.println("  Your Posts (" + myPosts.size() + "):");
        System.out.println("  " + "-".repeat(46));

        int index = 1;
        for (Post post : myPosts) {
            displayPost(index, post);
            index++;
        }
    }

    // ── Display a single post ─────────────────────────────
    private void displayPost(int index, Post post) {
        User author = userController.getUser(post.getAuthorUsername());
        String authorName = (author != null) ? author.getName() : post.getAuthorUsername();

        System.out.println("  [" + index + "] Post #" + post.getId()
                + "  |  " + authorName
                + "  |  " + post.getTimestamp());
        System.out.println("  " + post.getContent());
        System.out.println("  Likes: " + post.getLikes()
                + "   Comments: " + post.getComments().size());

        if (!post.getComments().isEmpty()) {
            System.out.println("  -- Comments --");
            for (String comment : post.getComments()) {
                System.out.println("    * " + comment);
            }
        }

        System.out.println("  " + "-".repeat(46));
    }
}