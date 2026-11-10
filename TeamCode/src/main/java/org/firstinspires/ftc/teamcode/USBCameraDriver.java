
package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCompatibilityManager;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.Collections;
import java.util.List;

public class USBCameraDriver {

    static final double MAX_YAW_DEG = 20;
    static final double MAX_PITCH_DEG = 25;
    static final double MAX_ROLL_DEG = 90;

    static final double IDEAL_RANGE = 50;
    static final double IDEAL_BEARING = 5;
    static final double IDEAL_ELEVATION = 15;
    static final double IDEAL_YAW = 0;

    static final double SCORE_THRESHOLD = 0.75;

    static final int VENDOR_ID_SUNPLUS_INNOVATION_TECHNOLOGY = 0x1BCF;
    static final int PRODUCT_ID_ARDUCAM_OV5648 = 0x284C;

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    private AprilTagDetection targetDetection;
    private List<AprilTagDetection> currentDetections = Collections.emptyList();

    private int targetTagId = -1;
    private String targetClusterName = null;

    private boolean closed = false;


    public USBCameraDriver(HardwareMap hm, String cameraHardwareName) {

        CameraCompatibilityManager.getInstance()
                .addQuirk(
                        VENDOR_ID_SUNPLUS_INNOVATION_TECHNOLOGY,
                        PRODUCT_ID_ARDUCAM_OV5648,
                        CameraCompatibilityManager.Quirk.AVOID_LIB_USB_RESET_DEVICE);

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
    }

    /**
     * updates the april tag detection
     */
    public void update() {

        if (closed) {
            return;
        }

        currentDetections = aprilTag.getDetections();

        if (currentDetections == null) {
            currentDetections = Collections.emptyList();
        }

        targetDetection = null;

        for (AprilTagDetection detection : currentDetections) {

            if (!matchesTarget(detection) || detection.ftcPose == null) {
                continue;
            }

            if (targetDetection == null ||
                    detection.ftcPose.range < targetDetection.ftcPose.range) {

                targetDetection = detection;
            }
        }
    }


    private boolean matchesTarget(AprilTagDetection detection) {

        if (targetTagId != -1) {

            if (!(detection instanceof AprilTagSingleDetection)) {
                return false;
            }

            AprilTagSingleDetection single =
                    (AprilTagSingleDetection) detection;

            return single.id == targetTagId;
        }

        if (targetClusterName != null) {

            if (!(detection instanceof AprilTagClusterDetection)) {
                return false;
            }

            AprilTagClusterDetection cluster =
                    (AprilTagClusterDetection) detection;

            return cluster.metadata != null &&
                    targetClusterName.equalsIgnoreCase(cluster.metadata.name);
        }

        return true;
    }


    public void setTargetTagId(int id) {
        targetTagId = id;
        targetClusterName = null;
    }


    public void setTargetClusterName(String name) {
        targetClusterName = name;
        targetTagId = -1;
    }


    public void clearTargetFilter() {
        targetTagId = -1;
        targetClusterName = null;
    }


    public boolean aprilTagDetected() {
        return targetDetection != null;
    }


    public AprilTagPoseFtc getPose() {

        if (!aprilTagDetected()) {
            return null;
        }

        return targetDetection.ftcPose;
    }


    public double getBearing() {

        AprilTagPoseFtc pose = getPose();

        return pose == null ? Double.NaN : pose.bearing;
    }


    public double getRange() {

        AprilTagPoseFtc pose = getPose();

        return pose == null ? Double.NaN : pose.range;
    }


    public int getTargetId() {

        if (targetDetection instanceof AprilTagSingleDetection) {
            return ((AprilTagSingleDetection) targetDetection).id;
        }

        return -1;
    }


    public boolean isGreen() {
        return calculateShotScore() >= SCORE_THRESHOLD;
    }



    /**
     * uses april tag positions to calculate shot score
     * @return double shot score
     */
    public double calculateShotScore() {
        
        if (!aprilTagDetected()) {
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
                yawError * yawError);

        double k = 0.03;

        double score = Math.exp(-k * error * error);

        return score;
    }


    public int getDetectionCount() {
        return currentDetections.size();
    }

    /**
     * just closes and ends everything
     */
    public void close() {
    
        if (!closed) {
            visionPortal.close();
            closed = true;
            targetDetection = null;
            currentDetections = Collections.emptyList();
        }
    }

    
}
