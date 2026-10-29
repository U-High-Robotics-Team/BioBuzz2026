package org.firstinspires.ftc.teamcode;

public class TransferDriver {
    
    private MotorSpeedController transferMotor;
    private MotorSpeedController feedMotor;

    public TransferDriver(HardwareMap hm, String transferMotor, String feedMotor){
        transferMotor = new MotorSpeedController(hm, transferMotor, 1000);
        feedMotor = new MotorSpeedController(hm, feedMotor, 1000);
    }

    public void tranferOn(){
    

    }

    public void transferOff(){
        motor1.setPower(0);
        feedMotor.setPower(0);
    }

    public int GetNumberOfBalls(){

    }

    private boolean ballReady(){

    }

}
