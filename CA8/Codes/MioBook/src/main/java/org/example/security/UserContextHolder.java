package org.example.security;

public class UserContextHolder {
    private static final ThreadLocal<UserDetailsFromToken> userHolder = new ThreadLocal<>();

    public static void set(UserDetailsFromToken user) {
        userHolder.set(user);
    }

    public static UserDetailsFromToken get() {
        return userHolder.get();
    }

    public static void clear() {
        userHolder.remove();
    }
}