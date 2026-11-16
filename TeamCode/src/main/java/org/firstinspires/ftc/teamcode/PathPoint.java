package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

// Creates a destination in X Y Heading(or H) and time to get to location in seconds and returns values when asked
public class PathPoint {

    //each tile is 60.95 cm x 60.95 cm
    final double r1_x, r1_y = -30.475, -152.375;
    final double r2_x, r2_y = -91.425,-152.375;
    final double r3_x, r3_y = -152.375,-91.425;
    final double r4_x, r4_y = -152.375,-30.475;
    final double r5_x, r5_y = -152.375, 30.475;
    final double r6_x, r6_y = -152.375, 152.375;
    final double r7_x, r7_y = -91.425, 152.375;
    final double r8_x, r8_y = -30.475, 152.375;

    final double b1_x, b1_y = 30.475, -152.375;
    final double b2_x, b2_y = 91.425,-152.375;
    final double b3_x, b3_y = 152.375,-152.375;
    final double b4_x, b4_y = 152.375,-30.475;
    final double b5_x, b5_y = 152.375, 30.475;
    final double b6_x, b6_y = 152.375, 91.425;
    final double b7_x, b7_y = 91.425, 152.375;
    final double b8_x, b8_y = 30.475, 152.375;


    private int x;
    private int y;
    private int h;
    private int timeLim;

    public PathPoint(int x, int y, int h, int timeLim){

        this.x = x; // distance x in cm from robot starting point
        this.y = y; // distance y in cm from robot starting point
        this.h = h; // degree in radian that robot want to rotate to (counter clock-wise)
        this.timeLim = timeLim; // limit for robot to attempt to get to point (in milliseconds) until time is up
    }

    public int giveX(){
        return this.x;
    }

    public int giveY(){
        return this.y;
    }

    public int giveH(){
        return this.h;
    }

    public int giveTimeLim(){
        return this.timeLim;
    }

}