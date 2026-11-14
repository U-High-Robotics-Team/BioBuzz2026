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
public class AutoMode extends OpMode {

    private ElapsedTime clock = new ElapsedTime();
    private State currentState;
    private MecanumDrive drive;
    private Localizer loc;
    private IntakeDriver intake;
    private TransferDriver transfer;
    private MotorSpeedController flywheel;
    private TurretDriver turret;
    private final int DELAY_TIME = 15; // delay for robot in seconds until we start to do tasks, ONLY used for AutoBD or AutoRD auto modes

    private enum State{
        TURNON,
        DELAY,
        MOVEFORWARD,
        SHOOT,
        TURNTURRET,
        PARK,
        STOP
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
            transfer.collect();
            intake.run();
            if(clock.seconds() >= 2){
                clock.reset();
                turret.resetTurretPos();
                currentState = State.MOVEFORWARD;
            }
        }
        if(currentState==State.DELAY){
            // Add wait if method usind DELAY_TIME 
        }
        if(currentState==State.MOVEFORWARD){
            if(drive.moveTo(250,10,0.5)){
                clock.reset();
                currentState = State.TURNTURRET;
            }
            
        }
        if(currentState==State.TURNTURRET){
            turret.turn(0.001);
            if(clock.seconds() >= 5){
                clock.reset();
                currentState = State.SHOOT;
            }
        }
        if(currentState==State.SHOOT){
            transfer.loadTurret();
            turret.startFlywheel();
            // will shoot after 2 seconds
            if(clock.seconds()>=2){
                clock.reset();
                currentState = State.PARK;
            } 
        }
        if(currentState==State.PARK){
            if(drive.moveTo(0,100,0)){ // Goes to parking area
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