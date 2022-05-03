// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.nashobarobotics.robot;

import com.nashobarobotics.lupine.Lupine;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
  private Lupine lupine;

  @Override
  public void robotInit() {
    Lupine.setVerbose(true);
    lupine = Lupine.init();
  }

  @Override
  public void teleopInit() {
    lupine.start();
  }

  @Override
  public void teleopExit() {
    lupine.stop();
  }


  @Override
  public void robotPeriodic() {}
  @Override
  public void teleopPeriodic() {}
  @Override
  public void disabledPeriodic() {}
  @Override
  public void simulationPeriodic() {}
}
