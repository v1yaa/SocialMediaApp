package socialmedia.view;

import socialmedia.controller.*;
import socialmedia.model.User;

import java.util.Scanner;

/**
 * VIEW: Displays all menus and handles console input/output.
 * Delegates every action to the appropriate Controller.
 * This is the only class that reads from the keyboard.
 */
public class MenuView {

    private Scanner                scanner              = new Scanner(System.in);

    // Controllers
    private UserController         userController;
    private FriendController       friendController;
    private PostController         postController;
    private NotificationController notifController;

    // Views
    private FeedView               feedView;

    // State
    private User currentUser = null;

    // ── Constructor ──────────────────────────────────────
    public MenuView(UserController userController,
                    FriendController friendController,
                    PostController postController,
                    NotificationController notifController,
                    FeedView feedView) {
        this.userController   = userController;
        this.friendController = friendController;
        this.postController   = postController;
        this.notifController  = notifController;
        this.feedView         = feedView;
    }

    // ─────────────────────────────────────────────────────
    //  TOP-LEVEL LOOP
    // ─────────────────────────────────────────────────────
    public void start() {
        System.out.println("=".repeat(50));
        System.out.println("      Welcome to SocialConnect Console App      ");
        System.out.println("=".repeat(50));

        while (true) {
            if (currentUser == null) showWelcomeMenu();
            else                     showMainMenu();
        }
    }

    // ─────────────────────────────────────────────────────
    //  WELCOME MENU  (not logged in)
    // ─────────────────────────────────────────────────────
    private void showWelcomeMenu() {
        System.out.println("\n  1. Sign Up");
        System.out.println("  2. Log In");
        System.out.println("  0. Exit");
        System.out.print("  Choice: ");

        switch (scanner.nextLine().trim()) {
            case "1": doSignUp();  break;
            case "2": doLogin();   break;
            case "0":
                System.out.println("\n  Goodbye! See you next time.");
                System.exit(0);
                break;
            default:
                System.out.println("  [!] Invalid option. Try again.");
        }
    }

    // ─────────────────────────────────────────────────────
    //  SIGN UP
    // ─────────────────────────────────────────────────────
    private void doSignUp() {
        printHeader("SIGN UP");
        System.out.print("  Username        : "); String username   = scanner.nextLine().trim();
        System.out.print("  Full Name       : "); String name       = scanner.nextLine().trim();
        System.out.print("  DOB (DD/MM/YYYY): "); String dob        = scanner.nextLine().trim();
        System.out.print("  Age             : ");
        int age = 0;
        try { age = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("  [!] Invalid age, set to 0."); }
        System.out.print("  Location        : "); String location   = scanner.nextLine().trim();
        System.out.print("  Occupation      : "); String occupation = scanner.nextLine().trim();

        userController.signup(username, name, dob, age, location, occupation);
    }

    // ─────────────────────────────────────────────────────
    //  LOG IN  (username only — no passwords for simplicity)
    // ─────────────────────────────────────────────────────
    private void doLogin() {
        printHeader("LOG IN");
        System.out.print("  Username: ");
        String username = scanner.nextLine().trim();

        User user = userController.getUser(username);
        if (user == null) {
            System.out.println("  [!] User not found. Please sign up first.");
        } else {
            currentUser = user;
            System.out.println("  [✓] Welcome back, " + user.getName() + "!");
        }
    }

    // ─────────────────────────────────────────────────────
    //  MAIN MENU  (logged in)
    // ─────────────────────────────────────────────────────
    private void showMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  Hello, " + currentUser.getName() + "!");
        notifController.showBadge(currentUser);
        System.out.println("=".repeat(50));
        System.out.println("  1. Friend Suggestions");
        System.out.println("  2. Friend Request Management");
        System.out.println("  3. View My Friends");
        System.out.println("  4. Create a Post");
        System.out.println("  5. News Feed");
        System.out.println("  6. Interact with a Post  (like / comment / share)");
        System.out.println("  7. Notifications");
        System.out.println("  8. My Posts");
        System.out.println("  9. Search User & Send Request");
        System.out.println("  0. Log Out");
        System.out.print("  Choice: ");

        switch (scanner.nextLine().trim()) {
            case "1": menuFriendSuggestions();    break;
            case "2": menuFriendRequests();       break;
            case "3":
                printHeader("MY FRIENDS");
                userController.viewFriends(currentUser);
                pressEnter();
                break;
            case "4": menuCreatePost();          break;
            case "5":
                printHeader("NEWS FEED");
                feedView.displayNewsFeed(currentUser);
                pressEnter();
                break;
            case "6": menuInteractPost();        break;
            case "7":
                printHeader("NOTIFICATIONS");
                notifController.viewNotifications(currentUser);
                pressEnter();
                break;
            case "8":
                printHeader("MY POSTS");
                feedView.displayMyPosts(currentUser);
                pressEnter();
                break;
            case "9": menuSearchUser();          break;
            case "0":
                System.out.println("  [✓] Logged out. Bye, " + currentUser.getName() + "!");
                currentUser = null;
                break;
            default:
                System.out.println("  [!] Invalid option.");
        }
    }

    // ─────────────────────────────────────────────────────
    //  SUB-MENUS
    // ─────────────────────────────────────────────────────

    private void menuFriendSuggestions() {
        printHeader("FRIEND SUGGESTIONS");
        userController.showFriendSuggestions(currentUser);

        System.out.println("\n  Would you like to send a friend request? (y/n)");
        System.out.print("  Choice: ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.print("  Enter username: ");
            friendController.sendRequest(currentUser, scanner.nextLine().trim());
        }
        pressEnter();
    }

    private void menuFriendRequests() {
        printHeader("FRIEND REQUEST MANAGEMENT");
        System.out.println("  1. View Pending Requests");
        System.out.println("  2. Accept / Reject a Request");
        System.out.println("  0. Back");
        System.out.print("  Choice: ");

        switch (scanner.nextLine().trim()) {
            case "1":
                friendController.viewPendingRequests(currentUser);
                pressEnter();
                break;
            case "2":
                friendController.viewPendingRequests(currentUser);
                if (!currentUser.getReceivedRequests().isEmpty()) {
                    System.out.print("  Enter the requester's username: ");
                    String requester = scanner.nextLine().trim();
                    friendController.handleAcceptReject(currentUser, requester, scanner);
                }
                pressEnter();
                break;
            case "0":
                break;
            default:
                System.out.println("  [!] Invalid option.");
        }
    }

    private void menuCreatePost() {
        printHeader("CREATE POST");
        System.out.print("  What's on your mind?\n  > ");
        String content = scanner.nextLine().trim();
        if (!content.isEmpty()) {
            postController.createPost(currentUser, content);
        } else {
            System.out.println("  [!] Post content cannot be empty.");
        }
        pressEnter();
    }

    private void menuInteractPost() {
        printHeader("INTERACT WITH A POST");
        System.out.print("  Enter Post ID: ");
        try {
            int postId = Integer.parseInt(scanner.nextLine().trim());
            postController.interactWithPost(currentUser, postId, scanner);
        } catch (NumberFormatException e) {
            System.out.println("  [!] Please enter a valid numeric Post ID.");
        }
        pressEnter();
    }

    private void menuSearchUser() {
        printHeader("SEARCH USER");
        System.out.print("  Enter username to search: ");
        String target = scanner.nextLine().trim();

        User found = userController.getUser(target);
        if (found == null) {
            System.out.println("  [!] No user found with username '" + target + "'.");
        } else {
            System.out.println("\n  User Found:");
            System.out.println(found);
            System.out.print("\n  Send a friend request to this user? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                friendController.sendRequest(currentUser, target);
            }
        }
        pressEnter();
    }

    // ─────────────────────────────────────────────────────
    //  UTILITIES
    // ─────────────────────────────────────────────────────
    private void printHeader(String title) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  " + title);
        System.out.println("=".repeat(50));
    }

    private void pressEnter() {
        System.out.print("\n  Press ENTER to continue...");
        scanner.nextLine();
    }
}