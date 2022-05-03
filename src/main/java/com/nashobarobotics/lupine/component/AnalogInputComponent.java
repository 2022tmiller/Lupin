package com.nashobarobotics.lupine.component;

import edu.wpi.first.wpilibj.AnalogInput;

public class AnalogInputComponent extends Component {
    private AnalogInput input;

    public AnalogInputComponent(String name, AnalogInput input) {
        super(name, "DigitalInput");
        this.input = input;
    }

    
    /** 
     * @return int
     */
    public int getChannel() {
        return input.getChannel();
    }

    
    /** 
     * @return int
     */
    public int getValue() {
        return input.getValue();
    }
    
}
