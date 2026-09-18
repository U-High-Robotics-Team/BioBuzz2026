package org.firstinspires.ftc.teamcode;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Basic Teleop", group="Testing")  // on the DS, opmodes are sorted by gorup, then name
public class BasicTeleop extends OpMode
{
    private ElapsedTime clock = new ElapsedTime();
    private MecanumDrive drive;
    private GoBildaPinpointDriver odo;
    
    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        // Initialize the hardware variables. 
        odo = hardwareMap.get(GoBildaPinpointDriver.class, "odo");
        odo.setOffsets(-84.0, -168.0); //these are tuned for 3110-0002-0001 Product Insight #1
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        odo.resetPosAndIMU();
        drive = new MecanumDrive(hardwareMap, odo);

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
        double x = gamepad1.left_stick_y;
        double y = -gamepad1.left_stick_x;
        double r = gamepad1.right_stick_x;

        // state machine here
        // set actuators here
        drive.setPowerVector(x, y, r);
    }

    // Code to run ONCE after the driver hits STOP
    // Use to set safe shutdown position if needed, but keep it quick
    @Override
    public void stop() {
    }

}