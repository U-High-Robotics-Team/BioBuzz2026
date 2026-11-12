package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

public class ServoMotorController {
    
    // techicaly not its speed, it just is the added position that the servo attempts to reach
    private final double MAX_SPEED = 0.005;
    private final double TURN_POS_MAX = 0.95;
    private final double TURN_POS_MIN = 0.01;
    private Servo servo;

    public ServoMotorController(HardwareMap hm, String servoName) {
        servo = hm.get(Servo.class, servoName);
        servo.setPosition(0.5);
    }

    public void turn(double speed) {
        if(speed > MAX_SPEED){
            speed = MAX_SPEED;
        }
        if(speed < -MAX_SPEED){
            speed = -MAX_SPEED;
        }
        checkDirection();
        servo.setPosition(servo.getPosition() + speed);
    }

    private void checkDirection(){
        if(servo.getPosition() >= TURN_POS_MAX){
            servo.setDirection(Servo.Direction.REVERSE);
        }
        if(servo.getPosition() <= TURN_POS_MIN){
            servo.setDirection(Servo.Direction.FORWARD);
        }
    }
}