package com.nashobarobotics.lupine;

import edu.wpi.first.hal.HAL;

/**
 * Custom logger for Lupine.
 */
public class Logger {
    static boolean verbose = false;

    public static enum Level {
        DEBUG, INFO, WARN, ERROR
    }

    public static void debug(String msg) {
        logImpl(Level.DEBUG, msg);
    }

    public static void info(String msg) {
        logImpl(Level.INFO, msg);
    }

    public static void warn(String msg) {
        logImpl(Level.WARN, msg);
    }

    public static void error(String msg) {
        logImpl(Level.ERROR, msg);
    }

    public static void log(Level lvl, String msg) {
        logImpl(lvl, msg);
    }

    private static void logImpl(Level lvl, String msg) {
        if(lvl == Level.WARN || lvl == Level.ERROR) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StackTraceElement lastStackFrame = stackTrace[3];
            String location = lastStackFrame.toString();
            boolean isError = lvl == (Level.ERROR);
            HAL.sendError(isError, 1, false, msg, location, "", true);
        } else {
            if(lvl == Level.DEBUG) {
                if(verbose) {
                    System.out.println("[Lupine] DEBUG: " + msg);
                }
            } else {
                System.out.println("[Lupine] " + msg);
            }
        }
    }
}
