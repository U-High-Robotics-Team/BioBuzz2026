package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.Collections;
import java.util.List;

public class USBCameraDriver {

    private static final double MAX_YAW_DEG = 20;
    private static final double MAX_PITCH_DEG = 25;
    private static final double MAX_ROLL_DEG = 90;

    private static final double IDEAL_RANGE = 50;
    private static final double IDEAL_BEARING = 5;
    private static final double IDEAL_ELEVATION = 15;
    private static final double IDEAL_YAW = 0;

    private static final double SCORE_THRESHOLD = 0.75;

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    private AprilTagDetection targetDetection;

    private List<AprilTagDetection> currentDetections =
            Collections.emptyList();

    private int targetTagId = -1;
    private String targetTagName = null;

    private boolean closed = false;


    public USBCameraDriver(HardwareMap hm, String cameraHardwareName) {
        initAprilTag(hm, cameraHardwareName);
    }


    private void initAprilTag(HardwareMap hm, String cameraHardwareName) {

        aprilTag = new AprilTagProcessor.Builder()
                .setLensIntrinsics(545.584, 545.584, 350.588, 223.15)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();

        builder.setCamera(hm.get(WebcamName.class, cameraHardwareName));
        builder.setCameraResolution(new Size(640, 480));
        builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);

        builder.addProcessor(aprilTag);

        visionPortal = builder.build();

        visionPortal.setProcessorEnabled(aprilTag, true);
    }


    public void update() {

        if (closed || visionPortal == null) {
            return;
        }

        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            currentDetections = Collections.emptyList();
            targetDetection = null;
            return;
        }

        currentDetections = aprilTag.getDetections();

        if (currentDetections == null) {
            currentDetections = Collections.emptyList();
        }

        targetDetection = null;

        for (AprilTagDetection detection : currentDetections) {

            if (!matchesTarget(detection)) {
                continue;
            }

            if (detection.ftcPose == null) {
                continue;
            }

            if (targetDetection == null ||
                    detection.ftcPose.range <
                    targetDetection.ftcPose.range) {

                targetDetection = detection;
            }
        }
    }


    private boolean matchesTarget(AprilTagDetection detection) {

        if (targetTagId != -1) {
            return detection.id == targetTagId;
        }

        if (targetTagName != null) {
            return detection.metadata != null &&
                    targetTagName.equalsIgnoreCase(detection.metadata.name);
        }

        return true;
    }


    public void setTargetTagId(int id) {
        targetTagId = id;
        targetTagName = null;
    }


    public void setTargetTagName(String name) {
        targetTagName = name;
        targetTagId = -1;
    }


    public void clearTargetFilter() {
        targetTagId = -1;
        targetTagName = null;
    }


    public boolean aprilTagDetected() {
        return targetDetection != null;
    }


    public AprilTagPoseFtc getPose() {

        if (targetDetection == null) {
            return null;
        }

        return targetDetection.ftcPose;
    }




    public int getTargetId() {
        return targetDetection == null ? -1 : targetDetection.id;
    }


    public boolean isGreen() {
        return calculateShotScore() >= SCORE_THRESHOLD;
    }

    public double calculateShotScore() {

        if (targetDetection == null || targetDetection.ftcPose == null) {
            return 0;
        }

        AprilTagPoseFtc pose = targetDetection.ftcPose;

        if (Math.abs(pose.yaw) > MAX_YAW_DEG) {
            return 0;
        }

        if (Math.abs(pose.pitch) > MAX_PITCH_DEG) {
            return 0;
        }

        if (Math.abs(pose.roll) > MAX_ROLL_DEG) {
            return 0;
        }

        double rangeError = (pose.range - IDEAL_RANGE) / 6.0;
        double bearingError = (pose.bearing - IDEAL_BEARING) / 4.0;
        double elevationError = (pose.elevation - IDEAL_ELEVATION) / 4.0;
        double yawError = (pose.yaw - IDEAL_YAW) / 10.0;

        double error = Math.sqrt(
                rangeError * rangeError +
                bearingError * bearingError +
                elevationError * elevationError +
                yawError * yawError
        );

        double k = 0.03;

        return Math.exp(-k * error * error);
    }

    public double autoAimRotation() {

        return 0;
    }

    public void close() {

        if (!closed && visionPortal != null) {
            visionPortal.close();

            closed = true;
            targetDetection = null;
            currentDetections = Collections.emptyList();
        }
    }
}