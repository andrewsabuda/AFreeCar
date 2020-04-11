package com.example.afreecar.model;

import com.example.afreecar.model.checklist.assembly.Part;
import com.example.afreecar.model.checklist.assembly.Terminal;
import com.example.afreecar.model.checklist.PartTag;
import com.example.afreecar.model.checklist.identification.PartRequirement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class containing static instances of other objects to be used for demo/testing purposes.
 */
public final class TestConstants {

    // Kit ID
    public static final ID KIT_ID = new ID("0");

    // Part IDs
    public static final ID CONTROLLER_ID = new ID("1");
    public static final ID BATTERY_ID = new ID("2");
    public static final ID MOTOR_1_ID = new ID("3");
    public static final ID MOTOR_2_ID = new ID("4");

    // Terminal IDs
    public static final ID CONTROLLER_TO_BATTERY_TERMINAL_ID = new ID("5");
    public static final ID CONTROLLER_TO_MOTOR_1_TERMINAL_ID = new ID("6");
    public static final ID CONTROLLER_TO_MOTOR_2_TERMINAL_ID = new ID("7");
    public static final ID BATTERY_TO_CONTROLLER_TERMINAL_ID = new ID("8");
    public static final ID MOTOR_1_TO_CONTROLLER_TERMINAL_ID = new ID("9");
    public static final ID MOTOR_2_TO_CONTROLLER_TERMINAL_ID = new ID("10");



    // PartTags
    public static final PartTag CONTROLLER_TAG = new PartTag(PartType.Controller, 1);
    public static final PartTag BATTERY_TAG = new PartTag(PartType.Battery, 1);
    public static final PartTag MOTOR_1_TAG = new PartTag(PartType.Motor, 1);
    public static final PartTag MOTOR_2_TAG = new PartTag(PartType.Motor, 2);


    // PartsRequirements
    public static final PartRequirement CONTROLLER_REQ = new PartRequirement(CONTROLLER_TAG, new HashSet<ID>(Arrays.asList(CONTROLLER_ID)));
    public static final PartRequirement BATTERY_REQ = new PartRequirement(BATTERY_TAG, new HashSet<ID>(Arrays.asList(BATTERY_ID)));
    public static final PartRequirement MOTOR_1_REQ = new PartRequirement(MOTOR_1_TAG, new HashSet<ID>(Arrays.asList(MOTOR_1_ID, MOTOR_2_ID)));
    public static final PartRequirement MOTOR_2_REQ = new PartRequirement(MOTOR_2_TAG, new HashSet<ID>(Arrays.asList(MOTOR_1_ID, MOTOR_2_ID)));

    // Kit
    public static final Kit KIT = new Kit(KIT_ID, CONTROLLER_REQ, BATTERY_REQ, MOTOR_1_REQ, MOTOR_2_REQ);

    // Basic ideal distance from QR code to edge of part/terminal
    public static final Double STANDARD_QR_DISTANCE = 0.5;

    // TerminalInfo
    public static final Terminal CONTROLLER_TO_BATTERY_TERMINAL = new Terminal(CONTROLLER_TO_BATTERY_TERMINAL_ID, STANDARD_QR_DISTANCE, BATTERY_TAG);
    public static final Terminal CONTROLLER_TO_MOTOR_1_TERMINAL = new Terminal(CONTROLLER_TO_MOTOR_1_TERMINAL_ID, STANDARD_QR_DISTANCE, MOTOR_1_TAG);
    public static final Terminal CONTROLLER_TO_MOTOR_2_TERMINAL = new Terminal(CONTROLLER_TO_MOTOR_2_TERMINAL_ID, STANDARD_QR_DISTANCE, MOTOR_2_TAG);
    public static final Terminal BATTERY_TO_CONTROLLER_TERMINAL = new Terminal(BATTERY_TO_CONTROLLER_TERMINAL_ID, STANDARD_QR_DISTANCE, CONTROLLER_TAG);
    public static final Terminal MOTOR_1_TO_CONTROLLER_TERMINAL = new Terminal(MOTOR_1_TO_CONTROLLER_TERMINAL_ID, STANDARD_QR_DISTANCE, CONTROLLER_TAG);
    public static final Terminal MOTOR_2_TO_CONTROLLER_TERMINAL = new Terminal(MOTOR_2_TO_CONTROLLER_TERMINAL_ID, STANDARD_QR_DISTANCE, CONTROLLER_TAG);


    // Part
    public static final Part CONTROLLER = new Part(
            CONTROLLER_ID,
            STANDARD_QR_DISTANCE,
            CONTROLLER_TO_BATTERY_TERMINAL,
            CONTROLLER_TO_MOTOR_1_TERMINAL,
            CONTROLLER_TO_MOTOR_2_TERMINAL
    );

    public static final Part BATTERY = new Part(
            BATTERY_ID,
            STANDARD_QR_DISTANCE,
            BATTERY_TO_CONTROLLER_TERMINAL
    );

    public static final Part MOTOR_1 = new Part(
            MOTOR_1_ID,
            STANDARD_QR_DISTANCE,
            MOTOR_1_TO_CONTROLLER_TERMINAL
    );

    public static final Part MOTOR_2 = new Part(
            MOTOR_2_ID,
            STANDARD_QR_DISTANCE,
            MOTOR_2_TO_CONTROLLER_TERMINAL
    );
}