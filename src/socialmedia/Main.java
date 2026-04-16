package socialmedia;

import socialmedia.controller.*;
import socialmedia.model.User;
import socialmedia.view.*;

public class Main {

    public static void main(String[] args) {

        // Create controllers
        UserController userController = new UserController();
        FriendController friendController = new FriendController(userController);
        PostController postController = new PostController(userController);
        NotificationController notifController = new NotificationController();

        // Create views
        FeedView feedView = new FeedView(postController, userController);
        MenuView menuView = new MenuView(userController, friendController, postController, notifController, feedView);

        // Load sample data
        loadSampleData(userController, postController);

        // Start the app
        menuView.start();
    }

    private static void loadSampleData(UserController uc, PostController pc) {
        uc.signup("alice", "Alice Smith", "15/06/1999", 25, "New York",      "Engineer");
        uc.signup("bob", "Bob Johnson", "22/06/1999", 25, "San Francisco", "Designer");
        uc.signup("carol", "Carol White", "03/11/1997", 27, "New York",      "Teacher");
        uc.signup("dave", "Dave Brown", "10/03/2000", 24, "Los Angeles",   "Developer");

        User alice = uc.getUser("alice");
        User bob = uc.getUser("bob");
        User carol = uc.getUser("carol");
        User dave = uc.getUser("dave");

        alice.addFriend("carol"); carol.addFriend("alice");
        bob.addFriend("dave"); dave.addFriend("bob");

        pc.createPost(alice, "Enjoying the sunny day in New York! #blessed");
        pc.createPost(bob, "Just finished my new design project. Loving it!");
        pc.createPost(carol, "Teaching is the best job in the world. #proudteacher");

        pc.likePost(bob, 1);

        System.out.println("\n  [INFO] Sample accounts ready: alice | bob | carol | dave");
        System.out.println("         (Just type any username at Login)\n");
    }
}