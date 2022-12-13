// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Elevator extends SubsystemBase {
  /** Creates a new Elevator. */

  WPI_TalonFX elevatorMotor;

  public Elevator() {
    elevatorMotor = new WPI_TalonFX(Constants.ElevatorConstants.elevatorMotorID);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void raiseElevatorMax() {
    elevatorMotor.set(TalonFXControlMode.PercentOutput, 0.5);
    if (limitSwitch.get() && elevatorMotor.get()>0.0) {
      elevatorMotor.set(TalonFXControlMode.PercentOutput, 0.0);
    }
  }

  public void moveElevator(double rotateVelocity) { // move elevator up & down w/ joystick
   
    double output = rotateVelocity;

    if (rotateVelocity > 0.0 && limitSwitch.get()) {
        output = 0.0;
    }
    
    elevatorMotor.set(TalonSRXControlMode.PercentOutput, output);
  }

}
