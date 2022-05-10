package com.nashobarobotics.lupin.logging;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nashobarobotics.lupin.Logger;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.wpilibj.DataLogManager;

public class LogManager {
    private static boolean logConsole = false, logDataLog = false, logNT = false, logWeb = false;

    private static class DataLogEntry {
        DataType type;
        int entry;
        DataLog log;

        public DataLogEntry(DataLog log, DataType type, Path path) {
            this.type = type;
            this.log = log;
            this.entry = log.start(path.toString(), type.type);
        }

        public void append(double x) {
            if(type == DataType.DOUBLE) {
                log.appendDouble(entry, x, 0);
            }
        }

        public void append(String s) {
            if(type == DataType.STRING) {
                log.appendString(entry, s, 0);
            }
        }

        public void append(boolean b) {
            if(type == DataType.BOOLEAN) {
                log.appendBoolean(entry, b, 0);
            }
        }
    }

    private static Map<Path, DataLogEntry> logEntries = new HashMap<>();
    private static Map<Path, NetworkTableEntry> ntEntries = new HashMap<>();

    private static Set<LoggedObject> loggedObjects = new HashSet<>();

    public static void configLogTargets(int targets) {        
        logConsole = (targets & LogTarget.CONSOLE) > 0;
        logNT = (targets & LogTarget.NT) > 0;
        logDataLog = (targets & LogTarget.DATALOG) > 0;
        logWeb = (targets & LogTarget.WEB) > 0;
    }

    private static NetworkTableEntry path2table(Path p) {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("lupin");
        for(String s: p.parent().asList()) {
            table = table.getSubTable(s);
        }
        return table.getEntry(p.asList().get(p.size() - 1));
    }

    public static void register(Path p, DataType type) {
        logEntries.put(p, new DataLogEntry(DataLogManager.getLog(), type, p));
        ntEntries.put(p, path2table(p));
    }

    public static void register(Path p, DataType type, DataLog log) {
        logEntries.put(p, new DataLogEntry(log, type, p));
        ntEntries.put(p, path2table(p));
    }
    
    public static void logDouble(Path p, double x) {
        if(logDataLog) {
            logEntries.get(p).append(x);
        }
        if(logNT) {
            ntEntries.get(p).setDouble(x);
        }
        if(logConsole) {
            Logger.info(p.toString() + ": " + x);
        }
        if(logWeb) {
            // TODO log web
        }
    }

    public static void logString(Path p, String s) {
        if(logDataLog) {
            logEntries.get(p).append(s);
        }
        if(logNT) {
            ntEntries.get(p).setString(s);
        }
        if(logConsole) {
            Logger.info(p.toString() + ": " + s);
        }
        if(logWeb) {
            // TODO log web
        }
    }

    public static void logBoolean(Path p, boolean b) {
        if(logDataLog) {
            logEntries.get(p).append(b);
        }
        if(logNT) {
            ntEntries.get(p).setBoolean(b);
        }
        if(logConsole) {
            Logger.info(p.toString() + ": " + b);
        }
        if(logWeb) {
            // TODO log web
        }
    }

    public static void registerObject(Path p, Object o) {
        LoggedObject lo = new LoggedObject(p, o.getClass());
        lo.setObject(o);
        loggedObjects.add(lo);
        lo.register();
    }

    public static void registerObject(Object o) {
        registerObject(new Path(o.getClass().getSimpleName()), o);
    }

    public static void logObjects() {
        for(LoggedObject lo: loggedObjects) {
            lo.log();
        }
    }
}
