package com.team3925.frc2018;

import com.team3925.frc2018.commands.DriveManual.DriveManualInput;
import com.team3925.frc2018.commands.SetSuperStructureState;
import com.team3925.frc2018.commands.ShiftHigh;
import com.team3925.frc2018.commands.ShiftLow;
import com.team3925.frc2018.commands.TuneElevator;
import com.team3925.frc2018.subsystems.Arm.ArmState;
import com.team3925.frc2018.subsystems.Elevator;
import com.team3925.frc2018.subsystems.Elevator.ElevatorState;
import com.team3925.frc2018.subsystems.Intake;
import com.team3925.frc2018.subsystems.Intake.IntakeState;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;

public class OI implements DriveManualInput {

	private final Joystick wheel;
	private final Joystick xbox;
	private final Joystick stick;

	private static OI instance;

	private Button drivetrain_Shift;

	private Button jogElevatorTop;
	private Button jogElevatorScaleHigh;
	private Button jogElevatorScaleMiddle;
	private Button jogElevatorScaleLow;
	private Button jogElevatorSwitch;
	private Button jogElevatorBottom;
	private Trigger dropCube;
	private Trigger shootCube;
	private Trigger shootBack;
	private Trigger switchShot;
	private Button intakeCube;
	private Button openGrabbers;

	private Trigger tuneUp;
	private Trigger tuneDown;

	public static OI getInstance() {
		if (instance == null)
			instance = new OI();
		return instance;
	}

	private OI() {
		stick = new Joystick(0);
		wheel = new Joystick(1);
		xbox = new Joystick(2);

		jogElevatorTop = new JoystickButton(xbox, 9);
		jogElevatorScaleHigh = new JoystickButton(xbox, 4);
		jogElevatorScaleMiddle = new JoystickButton(xbox, 2);
		jogElevatorScaleLow = new JoystickButton(xbox, 1);
		jogElevatorSwitch = new JoystickButton(xbox, 3);
		jogElevatorBottom = new JoystickButton(xbox, 10);
		
		drivetrain_Shift = new JoystickButton(wheel, 5);

		intakeCube = new JoystickButton(xbox, 6);
		openGrabbers = new JoystickButton(xbox, 5);

		dropCube = new Trigger() {
			@Override
			public boolean get() {
				return xbox.getPOV() == 180;
			}
		};
		
		shootBack = new Trigger() {
			@Override
			public boolean get() {
				return xbox.getPOV() == 90;
			}
		};

		shootCube = new Trigger() {

			@Override
			public boolean get() {
				return xbox.getPOV() == 0;
			}
		};

		tuneUp = new Trigger() {

			@Override
			public boolean get() {
				return xbox.getRawAxis(3) > 0.7;
			}
		};

		tuneDown = new Trigger() {
			@Override
			public boolean get() {			
				return xbox.getRawAxis(2) > 0.7;
			}
		};		
		
		switchShot = new Trigger() {
			
			@Override
			public boolean get() {
				return xbox.getPOV() == 270;
			}
		};


		jogElevatorTop.whenPressed(new SetSuperStructureState(ElevatorState.TOP, ArmState.SCALE_ANGLE, IntakeState.HOLD));
		jogElevatorScaleHigh.whenPressed(new SetSuperStructureState(ElevatorState.SCALE_MAX, ArmState.FORWARD_EXTENDED, IntakeState.HOLD));
		jogElevatorScaleMiddle.whenPressed(new SetSuperStructureState(ElevatorState.SCALE_MED, ArmState.FORWARD_EXTENDED, IntakeState.HOLD));
		jogElevatorScaleLow.whenPressed(new SetSuperStructureState(ElevatorState.SCALE_LOW, ArmState.FORWARD_EXTENDED, IntakeState.HOLD));
		jogElevatorSwitch.whenPressed(new SetSuperStructureState(ElevatorState.SWITCH, ArmState.FORWARD_EXTENDED, IntakeState.HOLD));
		jogElevatorBottom.whenPressed(new SetSuperStructureState(ElevatorState.BOTTOM, ArmState.RETRACTED, IntakeState.HOLD));
		
		shootBack.whenActive(new SetSuperStructureState(ElevatorState.UNKNOWN, ArmState.REVERSE_EXTENDED, IntakeState.SHOOT));
		shootBack.whenInactive(new SetSuperStructureState(ElevatorState.UNKNOWN, ArmState.RETRACTED, IntakeState.HOLD));
		dropCube.whenActive(new SetSuperStructureState(ElevatorState.UNKNOWN, ArmState.UNKNOWN, IntakeState.DROP));
		dropCube.whenInactive(new SetSuperStructureState(ElevatorState.UNKNOWN, ArmState.RETRACTED, IntakeState.HOLD));
		shootCube.whenActive(new SetSuperStructureState(ElevatorState.UNKNOWN, ArmState.UNKNOWN, IntakeState.SHOOT));
		shootCube.whenInactive(new SetSuperStructureState(ElevatorState.UNKNOWN, ArmState.RETRACTED, IntakeState.HOLD));

		drivetrain_Shift.whenPressed(new ShiftLow());
		drivetrain_Shift.whenReleased(new ShiftHigh());

		intakeCube.whenPressed(new SetSuperStructureState(ElevatorState.UNKNOWN, ArmState.FORWARD_EXTENDED, IntakeState.INTAKE));
		intakeCube.whenReleased(new SetSuperStructureState(ElevatorState.UNKNOWN, ArmState.RETRACTED, IntakeState.HOLD));
		
		openGrabbers.whenPressed(new SetSuperStructureState(ElevatorState.UNKNOWN, ArmState.UNKNOWN, IntakeState.OPEN));
		openGrabbers.whenReleased(new SetSuperStructureState(ElevatorState.UNKNOWN, ArmState.UNKNOWN, IntakeState.CLOSED));
		
		switchShot.whenActive(new SetSuperStructureState(ElevatorState.BOTTOM, ArmState.RETRACTED, IntakeState.ROLL_OUT));
		switchShot.whenInactive(new SetSuperStructureState(ElevatorState.UNKNOWN, ArmState.RETRACTED, IntakeState.HOLD ));

		tuneUp.whenActive(new TuneElevator(true));
		tuneDown.whenActive(new TuneElevator(false));
	}

	@Override
	public double getLeft() {
		return wheel.getRawAxis(0);
	}

	@Override
	public double getFwd() {
		return -stick.getRawAxis(1);
	}

	public double getElevator() {
		return -xbox.getRawAxis(1);
	}

	public double getLiftIntake() {
		return stick.getRawAxis(2);
	}

	public double getRawElevator() {
		return xbox.getRawAxis(5);
	}

	public boolean getTestButton() {
		return wheel.getRawButton(4);
	}
}
