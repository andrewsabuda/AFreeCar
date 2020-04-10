package com.example.afreecar.model;

import com.example.afreecar.model.checklist.assembly.PartConnectionsInfo;
import com.example.afreecar.model.checklist.assembly.Terminal;
import com.example.afreecar.model.checklist.PartTag;
import com.example.afreecar.model.checklist.identification.PartRequirement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class containing static instances of other objects to be used for demo/testing purposes.
 */
public class DemoStuff {

    // Part IDs
    public static final ID kitID = new ID("0");
    public static final ID controllerID = new ID("1");
    public static final ID batteryID = new ID("2");
    public static final ID motorID = new ID("3");



    // Terminal IDs
    public static final ID controllerToBatteryTerminalID = new ID("4");
    public static final ID controllerToMotorTerminalID = new ID("5");
    public static final ID batteryToControllerTerminalID = new ID("6");
    public static final ID motorToControllerTerminalID = new ID("7");



    // PartTags
    public static final PartTag controllerTag = new PartTag(PartType.Controller, 1);
    public static final PartTag batteryTag = new PartTag(PartType.Battery, 1);
    public static final PartTag motorTag = new PartTag(PartType.Motor, 1);



    // PartsRequirements
    public static final PartRequirement controllerReq = new PartRequirement(controllerTag, new HashSet<ID>(Arrays.asList(controllerID)));
    public static final PartRequirement batteryReq = new PartRequirement(batteryTag, new HashSet<ID>(Arrays.asList(batteryID)));
    public static final PartRequirement motorReq = new PartRequirement(motorTag, new HashSet<ID>(Arrays.asList(motorID)));



    // TerminalInfo
    public static final Terminal controllerToBatteryTerminalInfo = new Terminal(controllerToBatteryTerminalID, batteryTag);
    public static final Terminal controllerToMotorTerminalInfo = new Terminal(controllerToMotorTerminalID, motorTag);
    public static final Terminal batteryToControllerTerminalInfo = new Terminal(batteryToControllerTerminalID, controllerTag);
    public static final Terminal motorToControllerTerminalInfo = new Terminal(motorToControllerTerminalID, controllerTag);



    // TerminalInfo sets
    public static final Set<Terminal> controllerTerminals = new HashSet<Terminal>(Arrays.asList(
            controllerToBatteryTerminalInfo,
            controllerToMotorTerminalInfo
    ));

    public static final Set<Terminal> batteryTerminals = new HashSet<Terminal>(Arrays.asList(
            batteryToControllerTerminalInfo
    ));

    public static final Set<Terminal> motorTerminals = new HashSet<Terminal>(Arrays.asList(
            motorToControllerTerminalInfo
    ));



    // PartConnectionsInfo
    public static final PartConnectionsInfo controllerConnectionInfo = new PartConnectionsInfo(
            true,
            new HashSet<Terminal>(Arrays.asList(
                    controllerToBatteryTerminalInfo,
                    controllerToMotorTerminalInfo
                    )));

    public static final PartConnectionsInfo batteryConnectionInfo = new PartConnectionsInfo(
            true,
            new HashSet<Terminal>(Arrays.asList(
                    batteryToControllerTerminalInfo
            )));

    public static final PartConnectionsInfo motorConnectionInfo = new PartConnectionsInfo(
            true,
            new HashSet<Terminal>(Arrays.asList(
                    motorToControllerTerminalInfo
            )));
}