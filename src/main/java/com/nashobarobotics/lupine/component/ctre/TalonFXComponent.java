package com.nashobarobotics.lupine.component.ctre;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class TalonFXComponent extends CTREMotorControllerComponent {
    public TalonFXComponent(String name, TalonFX talon) {
        super(name, "TalonFX", talon);
    }
}
