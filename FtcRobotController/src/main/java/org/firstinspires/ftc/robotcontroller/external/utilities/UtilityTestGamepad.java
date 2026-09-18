package org.firstinspires.ftc.robotcontroller.external.utilities;
/*
        Copyright (c) 2026 Porpoiseful, LLC
        All rights reserved.
        Redistribution and use in source and binary forms, with or without modification,
        are permitted (subject to the limitations in the disclaimer below) provided that
        the following conditions are met:
        Redistributions of source code must retain the above copyright notice, this list
        of conditions and the following disclaimer.
        Redistributions in binary form must reproduce the above copyright notice, this
        list of conditions and the following disclaimer in the documentation and/or
        other materials provided with the distribution.
        Neither the name of Alan Smith nor the names of its contributors may be used to
        endorse or promote products derived from this software without specific prior
        written permission.
        NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
        LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
        "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
        THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
        ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
        FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
        DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
        SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
        CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
        TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
        THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.Gamepad;

/*
 * This OpMode helps test the gamepads so you can make sure they are functional.
 *
 */
@Utility(name = "Test Gamepad", description = "Test gamepads on your driver station")
@SuppressWarnings("unused")
public class UtilityTestGamepad extends OpMode {

    public static final String SEPARATOR = "  •  ";

    @Override
    public void init(){
        telemetry.addData("Instructions", "Setup your gamepads and then press the play button");
    }

    @Override
    public void loop() {
        display_gamepad(gamepad1, "Gamepad 1");
        display_gamepad(gamepad2, "Gamepad 2");
    }

    void display_gamepad(Gamepad gamepad, String name) {
        telemetry.addLine(name);
        telemetry.addData("Left Joystick",  "(% 1.2f, %1.2f)", gamepad.left_stick_x, gamepad.left_stick_y);
        telemetry.addData("Left Trigger", "% 1.2f", gamepad.left_trigger);
        telemetry.addData("Right Joystick",  "(% 1.2f, %1.2f)", gamepad.right_stick_x, gamepad.right_stick_y);
        telemetry.addData("Right Trigger", "% 1.2f", gamepad.right_trigger);

        switch(gamepad.type()){
            case SONY_PS4:
            case SONY_PS4_SUPPORTED_BY_KERNEL:

                if (gamepad.touchpad_finger_1) {
                    telemetry.addData("Touchpad Finger1", "(% 1.2f, %1.2f)", gamepad.touchpad_finger_1_x, gamepad.touchpad_finger_1_y);
                } else {
                    telemetry.addData("Touchpad Finger1", false);
                }

                if (gamepad.touchpad_finger_2) {
                    telemetry.addData("Touchpad Finger2", "(% 1.2f, %1.2f)", gamepad.touchpad_finger_2_x, gamepad.touchpad_finger_2_y);
                } else {
                    telemetry.addData("Touchpad Finger2", false);
                }

                telemetry.addData("Buttons", ps_buttons_to_string(gamepad));
                break;
            case LOGITECH_F310:
            case XBOX_360:
            case UNKNOWN:
            default:
                telemetry.addData("Buttons", xbox_buttons_to_string(gamepad));
                break;
        }
    }

    /*
      This returns a string based off of the buttons pressed, with the xbox names for buttons
     */
    String xbox_buttons_to_string(Gamepad gamepad){
        StringBuilder buttons = new StringBuilder(generic_buttons_to_string(gamepad));
        if (gamepad.back) buttons.append("back").append(SEPARATOR);
        if (gamepad.start) buttons.append("start").append(SEPARATOR);
        if (gamepad.guide) buttons.append("guide").append(SEPARATOR);
        if (gamepad.a) buttons.append("a").append(SEPARATOR);
        if (gamepad.b) buttons.append("b").append(SEPARATOR);
        if (gamepad.x) buttons.append("x").append(SEPARATOR);
        if (gamepad.y) buttons.append("y").append(SEPARATOR);

        return buttons.toString();
    }

    /*
      This returns a string based off of the buttons pressed, with the ps names for buttons
     */
    String ps_buttons_to_string(Gamepad gamepad){
        StringBuilder buttons = new StringBuilder(generic_buttons_to_string(gamepad));
        if (gamepad.cross) buttons.append("cross").append(SEPARATOR);
        if (gamepad.circle) buttons.append("circle").append(SEPARATOR);
        if (gamepad.square) buttons.append("square").append(SEPARATOR);
        if (gamepad.triangle) buttons.append("triangle").append(SEPARATOR);
        if (gamepad.ps) buttons.append("ps").append(SEPARATOR);
        if (gamepad.share) buttons.append("share").append(SEPARATOR);
        if (gamepad.options) buttons.append("options").append(SEPARATOR);
        if (gamepad.touchpad) buttons.append("touchpad").append(SEPARATOR);

        return buttons.toString();
    }

    /*
      This returns the buttons pressed for those that are the same on both types of gamepads
     */
    String generic_buttons_to_string(Gamepad gamepad){
        StringBuilder buttons = new StringBuilder();
        if (gamepad.dpad_up) buttons.append("dpad_up").append(SEPARATOR);
        if (gamepad.dpad_down) buttons.append("dpad_down").append(SEPARATOR);
        if (gamepad.dpad_left) buttons.append("dpad_left").append(SEPARATOR);
        if (gamepad.dpad_right) buttons.append("dpad_right").append(SEPARATOR);
        if (gamepad.left_bumper) buttons.append("left_bumper").append(SEPARATOR);
        if (gamepad.right_bumper) buttons.append("right_bumper").append(SEPARATOR);
        if (gamepad.left_stick_button) buttons.append("left stick button").append(SEPARATOR);
        if (gamepad.right_stick_button) buttons.append("right stick button").append(SEPARATOR);
        return buttons.toString();
    }

}
