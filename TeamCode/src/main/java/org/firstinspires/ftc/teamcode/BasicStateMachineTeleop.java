package org.firstinspires.ftc.teamcode;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Basic State Machine Teleop", group = "Testing") // on the DS, opmodes are sorted by gorup, then name
public class BasicStateMachineTeleop extends OpMode {
    
    private ElapsedTime clock = new ElapsedTime();
    private MotorPowerController motor312;
    private State currentState;
    
    private enum State {
        STOP,
        GO,
        PAUSE
    }

    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        currentState = State.STOP;
        // Initialize the hardware variables.
        motor312 = new MotorPowerController(hardwareMap, "motor1");

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Init OK");
    }

    // Code to run REPEATEDLY after the driver hits INIT, but before they hit START
    @Override
    public void init_loop() {
    }

    // Code to run ONCE when the driver hits START
    @Override
    public void start() {
        clock.reset();
    }

    // Code to run REPEATEDLY after the driver hits START but before they hit STOP
    // This is the main program loop
    @Override
    public void loop() {
        double throttle = gamepad1.left_stick_x;
        
        if (currentState == State.STOP){
            motor312.setPower(0);
            if (gamepad1.a){
                currentState = State.GO;
            }
        } else if (currentState == State.GO){
            motor312.setPower(throttle);
            if (gamepad1.b){
                currentState = State.STOP;
            } else if (gamepad1.x && throttle >= 0){
                currentState = State.PAUSE;
            }
        } else if (currentState == State.PAUSE) {
            if (gamepad1.x) {
                currentState = State.GO;
            }
        }

        telemetry.addData("State", currentState);
        telemetry.addData("Throttle", throttle);
    }

    // Code to run ONCE after the driver hits STOP
    // Use to set safe shutdown position if needed, but keep it quick
    @Override
    public void stop() {
    }

}
