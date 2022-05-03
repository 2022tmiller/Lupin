package com.nashobarobotics.lupine.component.ctre;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.nashobarobotics.lupine.component.CANComponent;

public abstract class CTREMotorControllerComponent extends CANComponent {
    private BaseMotorController controller;

    public CTREMotorControllerComponent(String name, String deviceType, BaseMotorController controller) {
        super(name, deviceType, controller.getDeviceID());
        this.controller = controller;
    }

    
    /** 
     * @return BaseMotorController
     */
    public BaseMotorController getController() {
        return this.controller;
    }
}
