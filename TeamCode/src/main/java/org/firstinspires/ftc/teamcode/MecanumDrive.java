package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;


public class MecanumDrive {

    private final double Kp = 0.0022; // bigger the error the faster we will fix it
    private final double Ki = 0.00013; // provides extra boost when you get close to the target
    private final double Kd = 0.00015; // dampens overshoot
    private final double WINDUP_LIMIT = 100; // caps integral sum to prevent windup
    private double integralSum = 0;
    private double lastError = 0;
    
    private MotorPowerController frontLeft;
    private MotorPowerController frontRight;
    private MotorPowerController backLeft;
    private MotorPowerController backRight;
    private GoBildaPinpointDriver odo;

     /**
     * Creates an object that controls four standard GoBilda mecanum wheels and yellowjacket gearmotors
     * The four motors must have these names in the Driver Console hardware configureation:
     * frontLeft, frontRight, backLeft, backRight. To use the field coordinate system for driving, 
     * the localizer method must be a valid GoBildaPinpointDriver object. (Other types
     * of localizers may be supported in the future.) Passing a null localizer will cause the 
     * mecanum drive to use the local robot coordinate system where +X is robot nose 
     * 
     * @param hm the hardware map inherited from OpMode (should be passed as simply 'hardwareMap') 
     */
    public MecanumDrive(HardwareMap hm, GoBildaPinpointDriver localizer){
        frontLeft = new MotorPowerController(hm, "frontLeft");
        frontRight = new MotorPowerController(hm, "frontRight");
        backLeft = new MotorPowerController(hm, "backLeft");
        backRight = new MotorPowerController(hm, "backRight");
         
        frontLeft.setDirection(1);
        frontRight.setDirection(1);
        backLeft.setDirection(-1);
        backRight.setDirection(-1);

        odo = null;
    }

    public void setPowerVector(double x, double y, double r){
        Pose2D pose = null;
        double heading = 0;
        if (odo != null){
            pose = odo.getPosition();
            heading = pose.getHeading(AngleUnit.RADIANS);
        }
        
        double cosAngle = Math.cos((Math.PI / 2) - heading);
        double sinAngle = Math.sin((Math.PI / 2) - heading);

        double globalX = x * cosAngle + y * sinAngle;
        double globalY = -x * sinAngle + y * cosAngle;
    
        double[] wheelPowers = new double[4];

        wheelPowers[0] = globalX - globalY + r;
        wheelPowers[1] = globalX - globalY - r;
        wheelPowers[2] = globalX + globalY + r;
        wheelPowers[3] = globalX + globalY - r;

        frontLeft.setPower(wheelPowers[0]);
        frontRight.setPower(wheelPowers[1]);
        backLeft.setPower(wheelPowers[2]);
        backRight.setPower(wheelPowers[3]);
    }

    public void moveTo(double targetX, double targetY, double targetHeading) {
        odo.update();

        // Getting current positions
        Pose2D currentPosition = odo.getPosition();
        double currentX = currentPosition.getX(DistanceUnit.MM);
        double currentY = currentPosition.getY(DistanceUnit.MM);
        double currentHeading = currentPosition.getHeading(AngleUnit.RADIANS);

         // Finding errors using current and targets
        double deltaX = targetX - currentX;
        double deltaY = targetY - currentY;
        double deltaHeading = targetHeading - currentHeading;

         // Accounting for minor errors
        if (Math.abs(deltaY) < 0.5) {
            deltaY = 0;
        }
        if (Math.abs(deltaX) < 0.5) {
            deltaX = 0;
        }
        if (Math.abs(deltaHeading) < 0.001) {
            deltaHeading = 0;
        }

        double xPower = deltaX * Kp;
        double yPower = deltaY * Kp;
        double turnPower = -deltaHeading;

        // Negative currentHeading due to global rotation being counterclockwise
        double cosAngle = Math.cos(-currentHeading);
        double sinAngle = Math.sin(-currentHeading);

        // Use inverse rotational matrix
        double localX = xPower * cosAngle + yPower * sinAngle;
        double localY = -xPower * sinAngle + yPower * cosAngle;

        // Calculate individual wheel powers
        double[] wheelPowers = new double[4];
        wheelPowers[0] = (localX - localY + turnPower);
        wheelPowers[1] = (localX - localY - turnPower);
        wheelPowers[2] = (localX + localY + turnPower);
        wheelPowers[3] = (localX + localY - turnPower);

        frontLeft.setPower(wheelPowers[0]);
        frontRight.setPower(wheelPowers[1]);
        backLeft.setPower(wheelPowers[2]);
        backRight.setPower(wheelPowers[3]);
    }

     public double findPIDPower(double delta){
        double out = 0;
        // TODO write PID controller
        //double derivative = 0;
        //double error = 0;

        //error = delta;

        // rate of change of the error
        //derivative = (delta - lastError) / timer.seconds();

        // sum of all error over time
        //out = (Kp * delta) + (Kd * derivative);

        //this.lastError = delta;

        return out;
    }
}
