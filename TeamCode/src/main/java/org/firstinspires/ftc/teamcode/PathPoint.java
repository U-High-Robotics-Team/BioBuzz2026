package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

// Creates a destination in X Y Heading(or H) and time to get to location in seconds and returns values when asked
// TODO add time in seconds to get to location
public class PathPoint {

    private int x;
    private int y;
    private int h;

    public Direction(int x, int y, int h){

        this.x = x;
        this.y = y;
        this.h = h;
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
}