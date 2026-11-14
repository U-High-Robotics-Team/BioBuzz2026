package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.State;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

// Remember to always reset clock @ end of specific state if you want timed events (unless you want to do more math)
@Autonomous
public class AutoModeR1 extends OpMode {

    private ElapsedTime clock = new ElapsedTime();
    private State currentState;
    private MecanumDrive drive;
    private Localizer loc;
    private IntakeDriver intake;
    private TransferDriver transfer;
    private MotorSpeedController flywheel;
    private TurretDriver turret;

    private enum State{
        TURNON,
        MOVEFORWARD,
        MOVEBACKWARD,
        SHOOT,
        TURNTURRET,
        PARK,
        STOP,
        DELAY
    }

    @Override 
    public void init(){
        currentState = State.TURNON;
        loc = new Localizer(hardwareMap, "gbpoc");
        drive = new MecanumDrive(hardwareMap, loc);
        intake = new IntakeDriver(hardwareMap, "intake");
        transfer = new TransferDriver(hardwareMap, "transfer", "feed");
        turret = new TurretDriver(hardwareMap, "flywheel", "turretServo");
        telemetry.addData("Status", "Init OK");
        telemetry.addData("Current State", currentState);
        
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {
        clock.reset();
    }

    public void loop(){
        loc.update();
        Pose2D currentLoc = loc.getPosition();
        telemetry.addData("State",currentState);
        int currX = (int)currentLoc.getX(DistanceUnit.MM);
        int currY = (int)currentLoc.getY(DistanceUnit.MM);
        int theta = (int)currentLoc.getHeading(AngleUnit.RADIANS);
        String position = String.format("x=%6d  y=%6d  r=%6d", currX, currY, theta);
        telemetry.addData("Position",position);
        if(currentState==State.TURNON){
            turret.resetTurretPos();
            transfer.collect();
            intake.run();
            if(clock.seconds() >= 2){
                clock.reset();
                turret.resetTurretPos();
                currentState = State.DELAY;
            }
        }
        if(currentState==State.DELAY) {
            if(clock.seconds() >= 13) {
                clock.reset();
                currentState = State.MOVEFORWARD;
            }
        }
        if(currentState==State.MOVEFORWARD){
            drive.moveTo(1320,-100,0);
            turret.turn(0.001);
            turret.startFlywheel();
            if(clock.seconds() >= 4){
                clock.reset();
                currentState = State.SHOOT;
            }
            
        }
        if(currentState==State.SHOOT){
            transfer.loadTurret();
            turret.startFlywheel();
            // will shoot after 3 seconds
            if(clock.seconds()>=3){
                clock.reset();
                currentState = State.MOVEBACKWARD;
            } 
        }
        if(currentState==State.MOVEBACKWARD){
            drive.moveTo(220,0,0);
            if(clock.seconds() >= 2){ // Goes to parking area
                clock.reset();
                currentState = State.PARK;
            }
        }
        if(currentState==State.PARK) {
           drive.moveTo(20,1500,0);
           if(clock.seconds() >= 4) {
                clock.reset();
                currentState = State.STOP;
           }
        }
        if(currentState==State.STOP){
            intake.stop();
            transfer.off();
            turret.stopFlywheel();
        }
    }
}