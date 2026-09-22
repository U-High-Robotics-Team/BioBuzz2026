package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.hardware.dfrobot.HuskyLens;

@TeleOp(name = "Test HuskyLens")
public class TestHuskyLens extends OpMode {

    private HuskyLens huskyLens;

    @Override
    public void init() {

    
        huskyLens = hardwareMap.get(HuskyLens.class, "huskylens");

        telemetry.addLine("HuskyLens initialized");
        telemetry.update();
    }

    @Override
    public void loop() {

        HuskyLens.Block[] blocks = huskyLens.blocks();

        telemetry.addData("Objects Detected", blocks.length);

        for (int i = 0; i < blocks.length; i++) {

            telemetry.addData(
                    "Object " + i,
                    "X=%d  Y=%d  Width=%d  Height=%d  ID=%d",
                    blocks[i].x,
                    blocks[i].y,
                    blocks[i].width,
                    blocks[i].height,
                    blocks[i].id
            );
        }

        telemetry.update();
    }
}
