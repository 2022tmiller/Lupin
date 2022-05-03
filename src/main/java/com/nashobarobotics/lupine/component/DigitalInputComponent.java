package com.nashobarobotics.lupine.component;

import edu.wpi.first.wpilibj.DigitalInput;

public class DigitalInputComponent extends Component {
    private DigitalInput input;

    public DigitalInputComponent(String name, DigitalInput input) {
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
     * @return boolean
     */
    public boolean getState() {
        return input.get();
    }
    
}
