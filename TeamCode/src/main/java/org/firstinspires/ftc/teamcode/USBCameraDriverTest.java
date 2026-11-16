package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;

import java.util.List;

@TeleOp(name = "USB Camera Driver Test", group = "Testing")

public class USBCameraDriverTest extends LinearOpMode {

    private static final String CAMERA_NAME = "Webcam 1";

    private USBCameraDriver camera;


    @Override
    public void runOpMode() throws InterruptedException {

        try {
            camera = new USBCameraDriver(hardwareMap, CAMERA_NAME);
            camera.clearTargetFilter();

        } catch (Exception e) {

            telemetry.addData("INITIALIZATION ERROR", e.toString());
            telemetry.update();
            return;
        }

        // Display camera information during INIT.

        while (!isStarted() && !isStopRequested()) {

            camera.update();
            displayCamera();

            telemetry.update();
            sleep(50);
        }

        waitForStart();

        while (opModeIsActive()) {

            camera.update();
            displayCamera();

            telemetry.update();
            sleep(20);
        }

        camera.close();
    }


    private void displayCamera() {

        telemetry.addData("Camera State", camera.getCameraState());
        telemetry.addData("Processor Enabled", camera.isProcessorEnabled());
        telemetry.addData("Camera FPS", "%.1f", camera.getFPS());

        telemetry.addData("Detection Count", camera.getDetectionCount());
        telemetry.addData("Any Tag Detected", camera.hasAnyAprilTag());
        telemetry.addData("Usable Pose Target", camera.aprilTagDetected());

        List<AprilTagDetection> detections = camera.getDetections();

        int index = 0;

        for (AprilTagDetection detection : detections) {

            telemetry.addLine("\n==== TAG " + index + " ====");

            telemetry.addData("ID", detection.id);

            if (detection.metadata != null) {
                telemetry.addData("Name", detection.metadata.name);
            } else {
                telemetry.addData("Name", "Unknown");
            }

            telemetry.addData(
                    "Center",
                    "X: %.0f  Y: %.0f",
                    detection.center.x,
                    detection.center.y
            );

            if (detection.ftcPose != null) {

                AprilTagPoseFtc pose = detection.ftcPose;

                telemetry.addData("Range", "%.2f", pose.range);
                telemetry.addData("Bearing", "%.2f", pose.bearing);
                telemetry.addData("Elevation", "%.2f", pose.elevation);

                telemetry.addData("Yaw", "%.2f", pose.yaw);
                telemetry.addData("Pitch", "%.2f", pose.pitch);
                telemetry.addData("Roll", "%.2f", pose.roll);

            } else {
                telemetry.addData("Pose", "Unavailable");
            }

            index++;
        }


        if (camera.aprilTagDetected()) {
            telemetry.addData("Target ID", camera.getTargetId());
            telemetry.addData("Shot Score", "%.3f",
                    camera.calculateShotScore());
            telemetry.addData("Green", camera.isGreen());

        } else {
            
        }
    }

}