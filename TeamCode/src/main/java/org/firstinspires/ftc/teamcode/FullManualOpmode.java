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
    private ElapsedTime timer = new ElapsedTime();
    private MecanumDrive drive;
    private Localizer loc;
    private IntakeDriver intake;
    private TransferDriver transfer;
    private TurretDriver turret;
    
    @Override
    public void init() {
        loc = new Localizer(hardwareMap, "gbpoc");
        drive = new MecanumDrive(hardwareMap, loc);
        intake = new IntakeDriver(hardwareMap, "intake"); //TODO check name
        transfer = new TransferDriver(hardwareMap, "transfer", "feed"); //TODO check name
        turret = new TurretDriver(hardwareMap, "flywheel","turretServo"); //TODO check name        
        
        telemetry.addData("Status", "Init OK");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        clock.reset();
        turret.resetTurretPos();
    }

    @Override
    public void loop() {
        
        loc.update();
        Pose2D currentLoc = loc.getPosition();
        int currX = (int)currentLoc.getX(DistanceUnit.MM);
        int currY = (int)currentLoc.getY(DistanceUnit.MM);
        int theta = (int)currentLoc.getHeading(AngleUnit.DEGREES);
        String position = String.format("x=%6d  y=%6d  r=%6d", currX, currY, theta);
        telemetry.addData("Position",position);
        telemetry.addData("Flywheel rpm",turret.getFlywheelRPM());
        double x = -gamepad1.left_stick_y;   // gamepad 'up' (-y) is robot +X
        double y = -gamepad1.left_stick_x;  // gamepad 'left' (-x) is robot +y
        double r = -gamepad1.right_stick_x; // gamepad 'left' (-x) is positive rotation
        
        drive.move(x, y, r);
        if (gamepad1.a){
            intake.run();
            transfer.collect();
        }
        if(gamepad2.a) {
            transfer.reverseOnlyTransferMotor();
        }
        if(gamepad1.dpad_down) {
            intake.reverse();
            transfer.reverse();
        }
        if (gamepad2.b){
            transfer.loadTurret();
            clock.reset();
        }
        if (gamepad2.x){
            turret.startFlywheel();
        }
         if (gamepad2.left_trigger > 0){
            turret.turn(0.003);
        }
        if (gamepad2.right_trigger > 0){
            turret.turn(-0.003);
        }
        if (gamepad1.y || gamepad2.y) {
            intake.stop();
            transfer.off();
            turret.stopFlywheel();
        }
        if (gamepad1.back || gamepad2.back) {
            loc = new Localizer(hardwareMap, "gbpoc");
        }

        //drive.moveTo(0,0,0);
    }

    @Override
    public void stop() {
        drive.stop();
        intake.stop();
        transfer.off();
    }
}