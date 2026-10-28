package org.firstinspires.ftc.teamcode;

public class Collect {

    private intakeMotor;

    public Collect(HardwareMap hm, String motorHardwareName) {
        intakeMotor = MotorPowerController(hm, motorHardwareName);
        intakeMotor.setDirection(1);
    }

    public void intake() {
        intakeMotor.setPower(1);
    }

    public void stop() {
        intakeMotor.setPower(0);
    }

    public void spitOut() {
        intakeMotor.setPower(-1);

    }
}
