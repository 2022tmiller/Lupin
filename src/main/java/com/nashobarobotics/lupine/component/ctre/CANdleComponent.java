package com.nashobarobotics.lupine.component.ctre;

import com.ctre.phoenix.led.CANdle;
import com.nashobarobotics.lupine.component.CANComponent;

public class CANdleComponent extends CANComponent {
    private CANdle candle;

    public CANdleComponent(String name, CANdle candle, int id) {
        super(name, "CANdle", id);
        this.candle = candle;
    }

    
    /** 
     * @return CANdle
     */
    public CANdle getCandle() {
        return this.candle;
    }
    
}
