package socialmedia.controller;

import socialmedia.model.User;

import java.util.Scanner;

/**
 * CONTROLLER: All business logic for Friend Request Management.
 * Handles sending, accepting, and rejecting friend requests.
 */
public class FriendController {

    private UserController userController;
    public FriendController(UserController userController) {
        this.userController = userController;
    }

    // ── Send a friend request ─────────────────────────────
    public void sendRequest(User sender, String targetUsername) {
        if (!userController.userExists(targetUsername)) {
            System.out.println("  [!] User '" + targetUsername + "' does not exist.");
            return;
        }
        if (sender.getUsername().equals(targetUsername)) {
            System.out.println("  [!] You cannot send a friend request to yourself.");
            return;
        }
        if (sender.isFriend(targetUsername)) {
            System.out.println("  [!] You are already friends with this user.");
            return;
        }
        if (sender.getSentRequests().contains(targetUsername)) {
            System.out.println("  [!] Friend request already sent to this user.");
            return;
        }
        User target = userController.getUser(targetUsername);

        // If target already sent ME a request, auto-accept both ways
        if (sender.getReceivedRequests().contains(targetUsername)) {
            acceptRequest(sender, targetUsername);
            return;
        }

        // Record request on both sides
        sender.getSentRequests().add(targetUsername);
        target.getReceivedRequests().add(sender.getUsername());

        // Notify target
        target.addNotification(sender.getName() + " sent you a friend request.");
        System.out.println("  [✓] Friend request sent to " + target.getName() + "!");
    }

    // ── View pending received requests ────────────────────
    public void viewPendingRequests(User me) {
        if (me.getReceivedRequests().isEmpty()) {
            System.out.println("  No pending friend requests.");
            return;
        }
        System.out.println("  Pending Friend Requests (" + me.getReceivedRequests().size() + "):");
        System.out.println("  " + "-".repeat(46));
        int index = 1;
        for (String requesterUsername : me.getReceivedRequests()) {
            User requester = userController.getUser(requesterUsername);
            if (requester != null) {
                System.out.println("  " + index + ". " + requester);
                System.out.println("  " + "-".repeat(46));
                index++;
            }
        }
    }

    // ── Accept a friend request ───────────────────────────
    public void acceptRequest(User me, String requesterUsername) {
        if (!me.getReceivedRequests().contains(requesterUsername)) {
            System.out.println("  [!] No request found from '" + requesterUsername + "'.");
            return;
        }
        User requester = userController.getUser(requesterUsername);
        if (requester == null) {
            System.out.println("  [!] User not found.");
            return;
        }

        // Build friendship on both sides
        me.addFriend(requesterUsername);
        requester.addFriend(me.getUsername());

        // Clean up request tracking
        me.getReceivedRequests().remove(requesterUsername);
        requester.getSentRequests().remove(me.getUsername());

        // Notify both users
        me.addNotification("You are now friends with " + requester.getName() + "!");
        requester.addNotification(me.getName() + " accepted your friend request!");
        System.out.println("  [✓] You are now friends with " + requester.getName() + "!");
    }

    // ── Reject a friend request ───────────────────────────
    public void rejectRequest(User me, String requesterUsername) {
        if (!me.getReceivedRequests().contains(requesterUsername)) {
            System.out.println("  [!] No request found from '" + requesterUsername + "'.");
            return;
        }
        User requester = userController.getUser(requesterUsername);
        me.getReceivedRequests().remove(requesterUsername);
        if (requester != null) {
            requester.getSentRequests().remove(me.getUsername());
        }
        System.out.println("  [✓] Friend request from '" + requesterUsername + "' rejected.");
    }

    // ── Accept / Reject prompt ────────────────────────────
    public void handleAcceptReject(User me, String requesterUsername, Scanner scanner) {
        System.out.print("  Accept or Reject this request? (a/r): ");
        String choice = scanner.nextLine().trim().toLowerCase();
        if (choice.equals("a")) acceptRequest(me, requesterUsername);
        else if (choice.equals("r")) rejectRequest(me, requesterUsername);
        else System.out.println("  [!] Invalid input. Returning to menu.");
    }
}
