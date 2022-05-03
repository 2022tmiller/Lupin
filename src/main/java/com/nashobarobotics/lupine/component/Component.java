package com.nashobarobotics.lupine.component;

public abstract class Component {
    private String name;
    private String deviceType = null;

    @SuppressWarnings("unused")
    private Component() {}

    /**
     * Create a Component with a given name. The default device type is null.
     * @param name The name for this Component
     */
    public Component(String name) {
        this.name = name;
    }

    /**
     * Create a Component with a given name and device type.
     * @param name The name for this Component
     * @param deviceType The device type for this Component
     */
    public Component(String name, String deviceType) {
        this.name = name;
        this.deviceType = deviceType;
    }

    /** 
     * Get the name of this Component
     * @return String The name
     * @see Component.setName(String name)
     */
    public final String getName() {
        return name;
    }

    
    /** 
     * Set the name of this Component
     * @param name The new name
     * @see Component.getName()
     */
    public final void setName(String name) {
        this.name = name;
    }

    
    /** 
     * Get this Component's device type
     * @return String The device type
     */
    public final String getDeviceType() {
        return deviceType;
    }

    
    /** 
     * Set this Component's device type
     * @param deviceType The new device type
     */
    protected final void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
