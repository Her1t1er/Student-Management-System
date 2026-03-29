/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author USER
 */
public class Session {
    private static boolean loggedIn = false;
    private static String username;

    public static void login(String user) {
        loggedIn = true;
        username = user;
    }

    public static void logout() {
        loggedIn = false;
        username = null;
    }

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static String getUsername() {
        return username;
    }
    
}
