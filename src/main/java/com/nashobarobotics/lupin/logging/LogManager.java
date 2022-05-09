package com.nashobarobotics.lupin.logging;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;

public class LogManager {
    private static Map<Path, DoubleLogEntry> doubleLogEntries = new HashMap<>();
    private static Map<Path, NetworkTableEntry> ntEntries = new HashMap<>();

    private static Set<LoggedObject> loggedObject = new HashSet<>();

    private static NetworkTableEntry path2table(Path p) {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("lupin");
        for(String s: p.parent().asList()) {
            table = table.getSubTable(s);
        }
        return table.getEntry(p.asList().get(p.size() - 1));
    }

    public static void registerDouble(Path p) {
        doubleLogEntries.put(p, new DoubleLogEntry(DataLogManager.getLog(), p.toString()));
        ntEntries.put(p, path2table(p));
    }
    
    public static void logDouble(Path p, double x) {
        doubleLogEntries.get(p).append(x);
        ntEntries.get(p).setDouble(x);
    }

    public static void registerObject(Path p, Object o) {
        
    }
}
