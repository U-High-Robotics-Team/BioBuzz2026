package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.HuskyLens;

private HuskyLens huskyLens;

public static final int FRAME_WIDTH = 320;
public static final int FRAME_HEIGHT = 240;


public class HuskyLensDriver{

    public HuskyLensDriver(HardwareMap hm, String cameraName){
        huskyLens = hm.get(HuskyLens.class, cameraName);
    }

    // Knock basically pings the camera to check if it is responsive before starting recognition.
    // We could change the type of recognition if we want 
    public void startRecognition(){
        if(huskyLens.knock()){
            huskyLens.setAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);
            huskyLens.startRecognition();
        }
    }
    public void stopRecognition(){
        huskyLens.stopRecognition();
    }
    public HuskyLens getHuskyLens(){
        return huskyLens;
    }
    /*
    Scans whats in the camera view and returns the largest detected block based on area. 
    The block with the largest area will be the block that is closest to the camera
    
    
    
    */
    public HuskyLens.Block getLargestBlock() {
        HuskyLens.Block[] blocks = huskyLens.blocks();
        HuskyLens.Block largestBlock = null;
        int maxArea = 0;

        for (HuskyLens.Block block : blocks) {
            int area = block.width * block.height;
            if (area > maxArea) {
                maxArea = area;
                largestBlock = block;
            }
        }
        return largestBlock;
    }

}