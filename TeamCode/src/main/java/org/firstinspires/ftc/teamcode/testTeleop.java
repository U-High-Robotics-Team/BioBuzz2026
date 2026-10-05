package org.firstinspires.ftc.teamcode;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import java.util.List;
import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.external.State;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="testTeleop", group="mai")  // on the DS, opmodes are sorted by gorup, then name
public class testTeleop extends OpMode {
    private ElapsedTime clock = new ElapsedTime();
    private MecanumDrive drive;
    private Localizer loc;
    private State currentState;
    List<Direction> list = new ArrayList<Direction>(); // each is "x" , "y" , "h" and is where the robot should move
    int occ; // New Variable for which occurence in the List<String> the robot should move to.
    private Direction superTest;
    private enum State {
        STOP,
        FOLLOWPATH
    }
    
    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        // Initialize the hardware variables. 
        loc = new Localizer(hardwareMap, "gbpoc");
        drive = new MecanumDrive(hardwareMap, loc);
        currentState = State.STOP;
        occ = 2;
        testAdd();
        
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
        telemetry.addData("Current: ", currentLoc);
        double x = gamepad1.left_stick_y;   // gamepad 'up' (+y) is robot +X
        double y = -gamepad1.left_stick_x;  // gamepad 'left' (-x) is robot +y
        double r = -gamepad1.right_stick_x; // gamepad 'left' (-x) is positive rotation

        // state machine here
        if (currentState == State.STOP){
            if (gamepad1.y){
                currentState = State.FOLLOWPATH;
            }
            if(gamepad1.x) {
                stop();
            }
            drive.move(x, y, r);
        }
        if (currentState == State.FOLLOWPATH){
            if((drive.moveTo(list.get(occ).giveX() , list.get(occ).giveY() , list.get(occ).giveH)) && ((occ+1) < list.size())){ //&& ((occ+3) < list.size()-1)
               occ++;
               telemetry.addData("\nCurrent occ: " + occ , occ);
            }else if (drive.moveTo(list.get(occ).giveX() , list.get(occ).giveY() , list.get(occ).giveH()) && (occ == list.size()-1)){
               currentState = State.STOP;
            }
            drive.moveTo(list.get(occ).giveX() , list.get(occ).giveY() , list.get(occ).giveH());
            if(gamepad1.x) {
                currentState = State.STOP;
            }
        }
        // set actuators here
        // drive.move(x, y, r); moved under State.STOP
        
    }

    // Code to run ONCE after the driver hits STOP
    // Use to set safe shutdown position if needed, but keep it quick
    @Override
    public void stop() {
    }

    public void testAdd() {
        superTest =  new Direction(0,100,0,loc);
        list.add(superTest);
        superTest = new Direction(100,0,0,loc);
        list.add(superTest);
        superTest = new Direction(0,-100,0,loc);
        list.add(superTest);
        superTest = new Direction(-100,0,0,loc);
        list.add(superTest);
    }

}
/**
 * int occ = 2; // at Start not in method loop
 * 
 *      if((drive.moveTo(Integer.parseInt(list.get(occ-2)) , Integer.parseInt(list.get(occ-1)) , Integer.parseInt(list.get(occ)))) && ((occ+2) < list.size()-1)){
 *              occ+=2;
 *       } else if (drive.moveTo(Integer.parseInt(list.get(occ-2)) , Integer.parseInt(list.get(occ-1)) , Integer.parseInt(list.get(occ))) && (occ == list.size()-1)){
 *              currentState = State.STOP;
 *       }
 *      drive.moveTo(Integer.parseInt(list.get(occ-2)) , Integer.parseInt(list.get(occ-1)) , Integer.parseInt.list.get((occ)));
 * 
 * 
 * Integer.parseInt()
 * 
 * 
 */