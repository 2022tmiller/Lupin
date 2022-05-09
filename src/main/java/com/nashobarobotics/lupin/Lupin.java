package com.nashobarobotics.lupin;

import java.util.HashSet;
import java.util.Set;

import com.nashobarobotics.lupin.web.WebSocketServer;

import org.eclipse.jetty.util.log.StdErrLog;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Class that runs one instance of Lupin. 
 */
public class Lupin {
    private static Set<Subsystem> subsystems = new HashSet<>();

    private static StdErrLog jettyLogger;
    static {
        jettyLogger = new StdErrLog();
        jettyLogger.setLevel(StdErrLog.LEVEL_WARN);
        org.eclipse.jetty.util.log.Log.setLog(jettyLogger);
    }

    private Lupin() {}

    /** 
     * Register a subsystem. Registering a subsystem will cancel any commands that require it while Lupin is running.
     * @param subsystem The subsystem to register
     */
    public static void registerSubsystem(Subsystem subsystem) {
        subsystems.add(subsystem);
    }

    private static class ReserveSubsystems implements Command {
        @Override
        public Set<Subsystem> getRequirements() {
            return subsystems;
        }
    }
}
