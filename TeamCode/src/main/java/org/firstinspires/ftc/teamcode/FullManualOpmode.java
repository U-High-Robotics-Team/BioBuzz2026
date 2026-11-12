package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.State;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@SuppressWarnings("unused")
@TeleOp(name = "Full Manual", group = "Testing") // on the DS, opmodes are sorted by gorup, then name
public class FullManualOpmode extends OpMode {

    private ElapsedTime clock = new ElapsedTime();
    private MecanumDrive drive;
    private Localizer loc;
    private IntakeDriver intake;
    private TransferDriver transfer;
    // private TurretDriver turret;
    //private ServoMotorController turret;
    private MotorSpeedController flywheel;
    
    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        // Initialize the hardware variables. 
        loc = new Localizer(hardwareMap, "gbpoc");
        drive = new MecanumDrive(hardwareMap, loc);
        intake = new IntakeDriver(hardwareMap, "intake"); //TODO check name
        transfer = new TransferDriver(hardwareMap, "transfer", "feed"); //TODO check name
        flywheel = new MotorSpeedController(hardwareMap, "flywheel", 6000);
        // turret = new TurretDriver(hardwareMap, "turretServo"); //TODO check name        

        
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
        
        loc.update();
        Pose2D currentLoc = loc.getPosition();
        int currX = (int)currentLoc.getX(DistanceUnit.MM);
        int currY = (int)currentLoc.getY(DistanceUnit.MM);
        int theta = (int)currentLoc.getHeading(AngleUnit.DEGREES);
        String position = String.format("x=%6d  y=%6d  r=%6d", currX, currY, theta);
        telemetry.addData("Position",position);
        telemetry.addData("Flywheel rpm",flywheel.getRPM());
        double x = -gamepad1.left_stick_y;   // gamepad 'up' (-y) is robot +X
        double y = -gamepad1.left_stick_x;  // gamepad 'left' (-x) is robot +y
        double r = -gamepad1.right_stick_x; // gamepad 'left' (-x) is positive rotation
        
        drive.move(x, y, r);
        if (gamepad1.a){
            intake.run();
            transfer.collect();
        }
        if (gamepad1.b){
            transfer.loadTurret();
        }
        if (gamepad1.x){
            flywheel.setRPM(3500);
        }
        if (gamepad1.y) {
            intake.stop();
            transfer.off();
            flywheel.coast();
        }

        //flywheel.setPower(0.75);
        //transfer.loadTurret();
        //drive.moveTo(0,0,0);
    }

    // Code to run ONCE after the driver hits STOP
    // Use to set safe shutdown position if needed, but keep it quick
    @Override
    public void stop() {
        drive.stop();
        intake.stop();
        transfer.off();
        flywheel.coast();
    }
}
