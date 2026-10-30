package org.firstinspires.ftc.teamcode;

public class TransferDriver {
    
    private MotorSpeedController transferMotor;
    private MotorSpeedController feedMotor;

    public TransferDriver(HardwareMap hm, String transferMotorName, String feedMotorName){
        this.transferMotor = new MotorSpeedController(hm, transferMotorName, 1000);
        this.feedMotor = new MotorSpeedController(hm, feedMotorName, 1000);
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
