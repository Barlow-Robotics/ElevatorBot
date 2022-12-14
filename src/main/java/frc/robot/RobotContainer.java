// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.RaiseElevatorMax;
import frc.robot.commands.RaiseElevatorMin;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Elevator;

import java.util.HashMap;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.*;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...
    
    private final DriveSubsystem driveSub = new DriveSubsystem();
    private final Elevator elevatorSub = new Elevator();

    private final RaiseElevatorMax raiseElevatorMaxCom = new RaiseElevatorMax(elevatorSub);
    private final RaiseElevatorMin raiseElevatorMinCom = new RaiseElevatorMin(elevatorSub);

    Joystick driverController; // Joystick 1
    Joystick operatorController; // Joystick 2

    private JoystickButton raiseElevatorMaxButton; // Button A operator
    private JoystickButton raiseElevatorMinButton; // Button B operator 

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        // Configure the button bindings
        configureButtonBindings();
        // loadTrajectories();
        // createAutonomousCommands();

        driveSub.setDefaultCommand(
                // A split-stick arcade command, with forward/backward controlled by the left
                // hand, and turning controlled by the right.
                new RunCommand( // new instance
                        () -> {
                            double x = -driverController.getRawAxis(Constants.Logitech_Dual_Action.Left_Stick_Y);
                            double yaw = driverController.getRawAxis(Constants.Logitech_Dual_Action.Right_Stick_X);
                            // fancy exponential formulas to shape the controller inputs to be flat when
                            // only
                            // pressed a little, and ramp up as stick pushed more.
                            double speed = 0.0;
                            if (x != 0) {
                                speed = (Math.abs(x) / x) * (Math.exp(-400.0 * Math.pow(x / 3.0, 4.0)))
                                        + (-Math.abs(x) / x);
                            }
                            double turn = -yaw;
                            // double turn = 0.0;
                            // if (yaw != 0) {
                            // turn = (Math.abs(yaw) / yaw) * (Math.exp(-400.0 * Math.pow(yaw / 3.0, 4.0)))
                            // + (-Math.abs(yaw) / yaw);
                            // }
                            // The turn input results in really quick movement of the bot, so
                            // let's reduce the turn input and make it even less if we are going faster
                            // This is a simple y = mx + b equation to adjust the turn input based on the
                            // speed.
                            // turn = turn * (-0.4 * Math.abs(speed) + 0.5);

                            driveSub.drive(-speed, -turn * 0.4, false);
                        },
                        driveSub));

    }

    elevatorSub.setDefaultCommand(
        // A split-stick arcade command, with forward/backward controlled by the left
        // hand, and turning controlled by the right.
        new RunCommand( // new instance
                () -> {
                    double yaw = operatorController.getRawAxis(Constants.Logitech_Dual_Action.Right_Stick_X)
                            * Constants.ElevatorConstants.maxElevatorOutput;
                    elevatorSub.elevatorMotor.moveElevator(yaw);
                },
                elevatorSub));

    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
     * it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        if (driverController == null) {
            System.out.println("Null driver controller, using joystick 1");
            driverController = new Joystick(1);
        }

        if (operatorController == null) {
            System.out.println("Null operator controller, using joystick 2");
            operatorController = new Joystick(2);
        }

        String controllerType = driverController.getName();
        System.out.println("The controller name is " + controllerType);
        // boolean controllerFound = false;

        raiseElevatorMaxButton = new JoystickButton(operatorController, Constants.Logitech_Dual_Action.Button_A);
        raiseElevatorMinButton = new JoystickButton(operatorController, Constants.Logitech_Dual_Action.Button_B);

        raiseElevatorMaxButton.whenPressed(raiseElevatorMaxCom);
        raiseElevatorMinButton.whenPressed(raiseElevatorMinCom);
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */

    // public Command getAutonomousCommand() {
    // This is just an example event map. It would be better to have a constant,
    // global event map
    // in your code that will be used by all path following commands.
    // HashMap<String, Command> eventMap = new HashMap<>();
    // eventMap.put("FirstBase", new PrintCommand("Passed first leg"));
    // eventMap.put("half way", new PrintCommand("half way there"));
    // eventMap.put("done", new PrintCommand("arrived at detination"));
    // eventMap.put("intakeDown", new IntakeDown());

    // This will load the file "Example Path.path" and generate it with a max
    // velocity of 4 m/s and a max acceleration of 3 m/s^2
    // PathPlannerTrajectory traj = PathPlanner.loadPath("SquarePath", new
    // PathConstraints(1, 1));
    // PathPlannerTrajectory traj = PathPlanner.loadPath("FancyPath", new
    // PathConstraints(2, 1));

    // Command ic = new InstantCommand(() -> {
    // // Reset odometry for the first path you run during auto
    // m_drive.resetEncoders();
    // m_drive.resetOdometry(traj.getInitialPose());
    // });

    // RamseteController controller = new RamseteController();

    // Command pathFollowingCommand = new PPRamseteCommand(
    // traj,
    // m_drive::getPose, // Pose supplier
    // controller,
    // new DifferentialDriveKinematics(0.75), // wpk need to put in correct chassis
    // width (wheel base)
    // m_drive::setWheelSpeeds,
    // eventMap, // This argument is optional if you don't use event markers
    // m_drive // Requires this drive subsystem
    // );
    // return new SequentialCommandGroup(ic, pathFollowingCommand);
    // }

}
