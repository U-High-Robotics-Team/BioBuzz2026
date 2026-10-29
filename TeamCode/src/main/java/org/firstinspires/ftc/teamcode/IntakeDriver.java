package org.firstinspires.ftc.teamcode;


public class IntakeDriver {
    //max 312
    private static final double MOTOR_SPEED = 120;

    private MotorSpeedController intakeMotor;

    public IntakeDriver(HardwareMap hm, String intakeMotorName) {
        //find correct rpm
        intakeMotor = new MotorSpeedController(hm, intakeMotorName, 0);
        //TODO check rotation direction
        intakeMotor.setDirection(1);
    }

    public void go() {
        intakeMotor.setRMP(MOTOR_SPEED);
    }

    public void stop() {
        intakeMotor.setRMP(0);
    }

    public void reverse() {
        intakeMotor.setRMP(-MOTOR_SPEED);

    }
}
