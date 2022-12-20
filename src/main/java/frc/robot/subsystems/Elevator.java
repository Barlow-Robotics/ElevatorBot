// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Elevator extends SubsystemBase {
  /** Creates a new Elevator. */

  public WPI_TalonFX elevatorMotor;
  DigitalInput limitSwitchTop;
  DigitalInput limitSwitchBottom;

  public Elevator() {
    elevatorMotor = new WPI_TalonFX(Constants.ElevatorConstants.elevatorMotorID);
    limitSwitchTop = new DigitalInput(Constants.ElevatorConstants.limitSwitchTopID);
    limitSwitchBottom = new DigitalInput(Constants.ElevatorConstants.limitSwitchBottomID);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void moveElevator(double rotateVelocity) { // move elevator up & down w/ joystick
   
    double output = rotateVelocity;

    if (rotateVelocity > 0.0 && limitSwitchTop.get()) {
      output = 0.0;
    }

    if (rotateVelocity < 0.0 && limitSwitchBottom.get()) {
      output = 0.0;
    }
    
    elevatorMotor.set(TalonFXControlMode.PercentOutput, output);
  }

  public void raiseElevatorMax() {
    elevatorMotor.set(TalonFXControlMode.PercentOutput, 0.5);
    
    if (limitSwitchTop.get() && elevatorMotor.get() > 0.0) {
      elevatorMotor.set(TalonFXControlMode.PercentOutput, 0.0);
    }
  }

  public void raiseElevatorMin() {
    elevatorMotor.set(TalonFXControlMode.PercentOutput, -0.5);
    
    if (limitSwitchBottom.get() && elevatorMotor.get() < 0.0) {
      elevatorMotor.set(TalonFXControlMode.PercentOutput, 0.0);
    }
  }

  public void moveToPosition(double desPos) {
    // desiredPosition is in inches 

    
  }

  public double getPosition() {
    return 0;
  }
}
