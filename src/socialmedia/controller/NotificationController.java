package socialmedia.controller;

import socialmedia.model.User;

/**
 * CONTROLLER: Business logic for Notifications.
 * Displays and clears notifications for the logged-in user.
 */
public class NotificationController {

    // ── Show all notifications then clear them ────────────
    public void viewNotifications(User user) {
        if (user.getNotifications().isEmpty()) {
            System.out.println("  No new notifications.");
            return;
        }
        System.out.println("  Notifications (" + user.getNotifications().size() + "):");
        System.out.println("  " + "-".repeat(46));
        int i = 1;
        for (String note : user.getNotifications()) {
            System.out.println("  " + i + ". " + note);
            i++;
        }
        System.out.println("  " + "-".repeat(46));
        user.clearNotifications();
        System.out.println("  [✓] All notifications marked as read.");
    }

    // ── Show a small badge count above the menu ───────────
    public void showBadge(User user) {
        int count = user.getNotifications().size();
        if (count > 0) {
            System.out.println("  *** You have " + count + " new notification(s)! ***");
        }
    }
}
