package com.nashobarobotics.lupine;

import java.util.HashSet;
import java.util.Set;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.nashobarobotics.lupine.component.AnalogInputComponent;
import com.nashobarobotics.lupine.component.Component;
import com.nashobarobotics.lupine.component.DigitalInputComponent;
import com.nashobarobotics.lupine.component.ObjectComponent;
import com.nashobarobotics.lupine.component.ctre.CANdleComponent;
import com.nashobarobotics.lupine.component.ctre.TalonFXComponent;
import com.nashobarobotics.lupine.web.WebSocketServer;

import org.eclipse.jetty.util.log.StdErrLog;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Class that runs one instance of Lupine. 
 */
public class Lupine {
    private static Lupine instance;

    private static Set<Component> components = new HashSet<>();
    private static Set<Subsystem> subsystems = new HashSet<>();

    private static StdErrLog jettyLogger;
    static {
        jettyLogger = new StdErrLog();
        jettyLogger.setLevel(StdErrLog.LEVEL_WARN);
        org.eclipse.jetty.util.log.Log.setLog(jettyLogger);
    }

    private ReserveSubsystems reserveSubsystems;
    private boolean running = false;
    private WebSocketServer wsserver;
    private int port;


    ////////////////////////////////////////
    //== Constructor and init() methods ==//
    ////////////////////////////////////////

    // Private constructor and init() methods insures that only one instance can be created per session
    private Lupine(int port) {
        instance = this;
        Logger.info("Initializing Lupine on port " + port + ". " + components.size() + " components registered.");
        this.port = port;
        reserveSubsystems = new ReserveSubsystems();
    }

    /**
     * Initialize Lupine with the default port number of 5808
     * @return The instance of Lupine
     * @see Lupine.init(int httpPort, int socketPort)
     */
    public static Lupine init() {
        return init(5808);
    }
    
    /** 
     * Initialize Lupine with a custom web socket port. Use this version of the init() method if another device or library is using port 5808.
     * @param port the port to use for the HTTP server
     * @return The instance of Lupine
     * @see Lupine.init()
     */
    public static Lupine init(int port) {
        if(instance != null) {
            throw new RuntimeException("Lupine already initialized. Make sure you are only calling Lupine.init() once.");
        }
        return new Lupine(port);
    }

    /**
     * Get the instance of Lupine. Only one instance may exist at a time.
     * @return The instance of Lupine
     */
    public static Lupine getInstance() {
        return instance;
    }
    
    ////////////////////////
    //== Static methods ==//
    ////////////////////////

    //== Registering components ==//

    /** 
     * Register a component. This will allow the properties of the component to be visible on the web interface.
     * @param component The component to register
     */
    public static void registerComponent(Component component) {
        components.add(component);
    }

    
    /** 
     * Register a digital input.
     * @param name The name of the digital input
     * @param input The DigitalInput instance
     * @see Lupine.registerComponent(Component component)
     */
    public static void registerDigitalInput(String name, DigitalInput input) {
        components.add(new DigitalInputComponent(name, input));
    }

    
    /** 
     * Register an analog input
     * @param name The name of the analog input
     * @param input The AnalogInput instance
     * @see Lupine.registerComponent(Component component)
     */
    public static void registerAnalogInput(String name, AnalogInput input) {
        components.add(new AnalogInputComponent(name, input));
    }

    
    /** 
     * Register a Talon FX
     * @param name The name of the Talon FX
     * @param device The TalonFX instance
     */
    public static void registerTalonFX(String name, TalonFX device) {
        components.add(new TalonFXComponent(name, device));
    }

    /** 
     * Register a CTRE CANdle
     * @param name The name of the CANdle
     * @param device The CANdle instance
     * @param device The CANdle's CAN id
     */
    public static void registerCANdle(String name, CANdle device, int canId) {
        components.add(new CANdleComponent(name, device, canId));
    }
    
    /** 
     * @param name
     * @param object
     */
    public static void registerObject(String name, Object object) {
        components.add(new ObjectComponent(name, object));
    }

    //== Other ==//

    /** 
     * Register a subsystem. Registering a subsystem will cancel any commands that require it while Lupine is running.
     * @param subsystem The subsystem to register
     */
    public static void registerSubsystem(Subsystem subsystem) {
        subsystems.add(subsystem);
    }

    /**
     * Enable or disable verbose logging.
     * @param verbose True to enable verbose logging, false to disable
     */
    public static void setVerbose(boolean verbose) {
        Logger.verbose = verbose;
        if(verbose) {
            jettyLogger.setLevel(StdErrLog.LEVEL_INFO);
        } else {
            jettyLogger.setLevel(StdErrLog.LEVEL_WARN);
        }
    }

    //////////////////////////
    //== Instance methods ==//
    //////////////////////////

    /**
     * Start Lupine. Calls to start() will have no effect if Lupine is already running.
     */
    public void start() {
        if(!running) {
            Logger.info("Starting Lupine.");
            running = true;
            CommandScheduler.getInstance().schedule(reserveSubsystems);
            wsserver = new WebSocketServer(port);
            wsserver.start();
        }
    }

    /**
     * Stop Lupine. Calls to stop() will have no effect if Lupine is not running.
     */
    public void stop() {
        if(running) {
            Logger.info("Stopping Lupine.");
            running = false;
            CommandScheduler.getInstance().cancel(reserveSubsystems);
            wsserver.interrupt();
            wsserver = null;
        }
    }

    ///////////////////////
    //== Inner classes ==//
    ///////////////////////

    private static class ReserveSubsystems implements Command {
        @Override
        public Set<Subsystem> getRequirements() {
            return subsystems;
        }
    }
}
