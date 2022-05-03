package com.nashobarobotics.lupine.component;

public abstract class CANComponent extends Component {
    private int id;

    public CANComponent(String name, int id) {
        super(name);
        this.id = id;
    }

    public CANComponent(String name, String deviceType, int id) {
        super(name, deviceType);
        this.id = id;
    }

    
    /** 
     * @return int
     */
    public final int getId() {
        return id;
    }
}
