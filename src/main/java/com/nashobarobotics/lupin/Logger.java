package com.nashobarobotics.lupin;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.DataLogManager;

/**
 * The Logger class encapsulates logging to the RioLog and DataLog, and handles critical error detection.
 */
public class Logger {
    public static enum Level {
        DEBUG, INFO, WARN, ERROR, CRITICAL
    }

    private static boolean enableVerboseLogging = false;
    private static boolean enableDataLog = false;
    private static boolean hasCriticalError = false;

    public static void configVerboseLogging(boolean verbose) {
        enableVerboseLogging = verbose;
    }

    public static void configDataLog(boolean dataLog) {
        enableDataLog = dataLog;
    }

    public static boolean hasCriticalError() { return hasCriticalError; }

    public static void debug(String msg) {
        logImpl(Level.DEBUG, msg, null);
    }

    public static void debug(String msg, Throwable t) {
        logImpl(Level.DEBUG, msg, t.getStackTrace()[0]);
    }

    public static void info(String msg) {
        logImpl(Level.INFO, msg, null);
    }

    public static void info(String msg, Throwable t) {
        logImpl(Level.INFO, msg, t.getStackTrace()[0]);
    }

    public static void warn(String msg) {
        logImpl(Level.WARN, msg, null);
    }

    public static void warn(String msg, Throwable t) {
        logImpl(Level.WARN, msg, t.getStackTrace()[0]);
    }

    public static void error(String msg) {
        logImpl(Level.ERROR, msg, null);
    }

    public static void error(String msg, Throwable t) {
        logImpl(Level.ERROR, msg, t.getStackTrace()[0]);
    }

    public static void critical(String msg) {
        logImpl(Level.ERROR, msg, null);
    }

    public static void critical(String msg, Throwable t) {
        logImpl(Level.ERROR, msg, t.getStackTrace()[0]);
    }

    public static void log(Level lvl, String msg) {
        logImpl(lvl, msg, null);
    }

    public static void log(Level lvl, String msg, Throwable t) {
        logImpl(lvl, msg, t.getStackTrace()[0]);
    }

    private static void logImpl(Level lvl, String msg, StackTraceElement source) {
        if(lvl == Level.DEBUG && !enableVerboseLogging) {
            return;
        }
        if(lvl == Level.CRITICAL) {
            hasCriticalError = true;
        }
        if(lvl == Level.WARN || lvl == Level.ERROR || lvl == Level.CRITICAL) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StackTraceElement lastStackFrame = stackTrace[3];
            String location = lastStackFrame.toString();
            boolean isError = (lvl != Level.WARN);
            if(lvl == Level.CRITICAL) {
                msg = "[CRITICAL ERROR] " + msg;
            }
            String callStack = "";
            if(source != null) {
                callStack = source.toString();
            }
            HAL.sendError(isError, 1, false, msg, location, callStack, true);
        } else {
            String message = "[" + lvl + "] " + msg;
            if(enableDataLog) {
                DataLogManager.log(message);
            } else {
                System.out.println(message);
            }
        }
    }
}
