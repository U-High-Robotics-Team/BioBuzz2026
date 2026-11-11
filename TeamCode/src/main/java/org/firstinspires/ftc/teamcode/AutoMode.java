package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.State;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

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
    private boolean onlyOnce;

    private enum State{
        TURNON,
        MOVEFORWARD,
        SHOOT,
        TURNTURRET,
        STOP
    }

    @Override 
    public void init(){
        currentState = State.TURNON;
        loc = new Localizer(hardwareMap, "gbpoc");
        drive = new MecanumDrive(hardwareMap, loc);
        intake = new IntakeDriver(hardwareMap, "intake");
        transfer = new TransferDriver(hardwareMap, "transfer", "feed");
        turret = new TurretDriver(hardwareMap, "flywheel", "turret");
        onlyOnce = true;
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
                currentState = State.MOVEFORWARD;
            }
        }
        if(currentState==State.MOVEFORWARD){
            if(!drive.moveTo(250,0,0)){
                drive.moveTo(250,0,0);
            }
            if(drive.moveTo(250,0,0)){
                currentState = State.SHOOT;
            }
        }
        if(currentState==State.SHOOT){
            if(onlyOnce){
                clock.reset();
                onlyOnce = false;
            }
            transfer.loadTurret();
            turret.setFlywheelRPM(6000);
            // will shoot after 2 seconds
            if(clock.seconds()>=2){
                currentState = State.STOP;
                onlyOnce = true;
            } 
        }
        if(currentState==State.STOP){
            intake.stop();
            transfer.off();
            turret.setFlywheelRPM(0);
        }
    }
}

