package org.firstinspires.ftc.teamcode;

public class TransferDriver {
    
    private MotorSpeedController transferMotor;
    private MotorSpeedController feedMotor;

    public TransferDriver(HardwareMap hm, String transferMotorName, String feedMotorName){
        this.transferMotor = new MotorSpeedController(hm, transferMotorName, 100);
        this.feedMotor = new MotorSpeedController(hm, feedMotorName, 100);
    }

    public void tranferOn(){
        this.transferMotor.setRPM(100);
        if (ballReady()){
            this.feedMotor.setRPM(100);
        } else if (!ballReady()){
            this.feedMotor.setRPM(-100);
        }
    }

    public void transferOff(){
        this.transferMotor.setRPM(0);
        this.feedMotor.setRPM(0);
    }

    public int getNumberOfBalls(){

    }

    private boolean ballReady(){
        if(getNumberOfBalls() >= 1){
            return true;
        }else if (getNumberOfBalls() == 0){
            return false;
        }
        return null;
    }

}
