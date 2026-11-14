/*
*   DO NOT MODIFY THIS FILE
*   This file exists as a reference for the standard way of creating a state machine opmode
*   Make a copy if you wish and modify that
*   DO NOT MODIFY THIS FILE
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.State;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@SuppressWarnings("unused")
@TeleOp(name = "FullManualOpmode", group = "Testing") // on the DS, opmodes are sorted by gorup, then name
public class FullManualOpmode extends OpMode {

    private ElapsedTime clock = new ElapsedTime();
    private MecanumDrive drive;
    private Localizer loc;
    private IntakeDriver intake;
    private MotorSpeedController flywheel;
    private TransferDriver transfer;
    private ServoMotorController turret;
    private State currentState;
    private enum State {
        STOP,
        COLLECT,
        SHOOT,
        SHOOT_REQUEST,
        REVERSE
    }
    
    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        // Initialize the hardware variables.
        loc = new Localizer(hardwareMap, "gbpoc");
        drive = new MecanumDrive(hardwareMap, loc);
        intake = new IntakeDriver(hardwareMap, "intake");
        //flywheel = new MotorSpeedController(hardwareMap, "flywheel", 6000);
        transfer = new TransferDriver(hardwareMap, "transfer", "feed");
        //turret = new ServoMotorController(hardwareMap, "turret");
        currentState = State.STOP;
        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Init OK");
    }

    // Code to run REPEATEDLY after the driver hits INIT, but before they hit START
    @Override
    public void init_loop() {
    }

    //Code to run ONCE when the driver hits START
    @Override
    public void start() {
        clock.reset();
    }

    // Code to run REPEATEDLY after the driver hits START but before they hit STOP
    // This is the main program loop
    @Override
    public void loop() {
        // read sensors here
        loc.update();
        Pose2D currentLoc = loc.getPosition();
        telemetry.addData("State",currentState);
        int currX = (int)currentLoc.getX(DistanceUnit.MM);
        int currY = (int)currentLoc.getY(DistanceUnit.MM);
        int theta = (int)currentLoc.getHeading(AngleUnit.DEGREES);
        String position = String.format("x=%6d  y=%6d  r=%6d", currX, currY, theta);
        telemetry.addData("Position",position);
        double x = -gamepad1.left_stick_y;   // gamepad 'up' (-y) is robot +X
        double y = -gamepad1.left_stick_x;  // gamepad 'left' (-x) is robot +y
        double r = -gamepad1.right_stick_x; // gamepad 'left' (-x) is positive rotation
        telemetry.addData("Flywheel RPM:", flywheel.getRPM());

        // STATE MACHINE START
        if (currentState == State.STOP){
            if(gamepad1.left_trigger > 0.5) {
                turret.turn(0.005);
            }
            if(gamepad1.right_trigger > 0.5) {
                turret.turn(0.005);
            }
            if(gamepad1.a) {
                currentState = State.COLLECT;
            }
            drive.move(x,y,r);
            intake.stop();
            transfer.off();
            flywheel.setRPM(0);

        }  else if (currentState == State.REVERSE) {
            if(gamepad1.left_trigger > 0.5) {
                turret.turn(0.005);
            }
            if(gamepad1.right_trigger > 0.5) {
                turret.turn(0.005);
            }
            if(gamepad1.y) {
                currentState = State.STOP;
            }
            if(gamepad1.a) {
                currentState = State.COLLECT;
            }
            drive.move(x,y,r);
            intake.reverse();
            transfer.reverse();
            flywheel.setRPM(0);

        } else if (currentState == State.COLLECT){
            if(gamepad1.left_trigger > 0.5) {
                turret.turn(0.005);
            }
            if(gamepad1.right_trigger > 0.5) {
                turret.turn(0.005);
            }
            if(gamepad1.left_bumper) {
                currentState = State.SHOOT_REQUEST;
            }
            if(gamepad1.y) {
                currentState = State.STOP;
            }
            if(gamepad1.b) {
                currentState = State.REVERSE;
            }
            drive.move(x, y, r);
            intake.run();
            transfer.collect();
            flywheel.setRPM(0);

        } else if (currentState == State.SHOOT_REQUEST) {
            if(gamepad1.left_trigger > 0.5) {
                turret.turn(0.005);
            }
            if(gamepad1.right_trigger > 0.5) {
                turret.turn(0.005);
            }
            if(gamepad1.y) {
                currentState = State.STOP;
            }
            if(gamepad1.a) {
                currentState = State.COLLECT;
            }
            drive.move(x,y,r);
            flywheel.setRPM(6000);
            if (flywheel.getRPM() > 5000) {
                currentState = State.SHOOT;
            }
            
        } else if (currentState == State.SHOOT) {
            if(gamepad1.left_trigger > 0.5) {
                turret.turn(0.005);
            }
            if(gamepad1.right_trigger > 0.5) {
                turret.turn(0.005);
            }
            if(gamepad1.y) {
                currentState = State.STOP;
            }
            if(gamepad1.a) {
                currentState = State.COLLECT;
            }
            drive.move(x,y,r);
            transfer.loadTurret();
            if(flywheel.getRPM() < 5000) {
                currentState = State.SHOOT_REQUEST;
            }
        }
        // STATE MACHINE END
    }

    // Code to run ONCE after the driver hits STOP
    // Use to set safe shutdown position if needed, but keep it quick
    @Override
    public void stop() {
        drive.stop();
    }
}