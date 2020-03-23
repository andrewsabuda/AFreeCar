package com.example.afreecar.model;

import com.example.afreecar.model.assembly.PartConnectionsInfo;
import com.example.afreecar.model.assembly.TerminalInfo;
import com.example.afreecar.model.identification.PartRequirement;

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
    public static final TerminalInfo controllerToBatteryTerminalInfo = new TerminalInfo(controllerToBatteryTerminalID, batteryTag);
    public static final TerminalInfo controllerToMotorTerminalInfo = new TerminalInfo(controllerToMotorTerminalID, motorTag);
    public static final TerminalInfo batteryToControllerTerminalInfo = new TerminalInfo(batteryToControllerTerminalID, controllerTag);
    public static final TerminalInfo motorToControllerTerminalInfo = new TerminalInfo(motorToControllerTerminalID, controllerTag);



    // TerminalInfo sets
    public static final Set<TerminalInfo> controllerTerminals = new HashSet<TerminalInfo>(Arrays.asList(
            controllerToBatteryTerminalInfo,
            controllerToMotorTerminalInfo
    ));

    public static final Set<TerminalInfo> batteryTerminals = new HashSet<TerminalInfo>(Arrays.asList(
            batteryToControllerTerminalInfo
    ));

    public static final Set<TerminalInfo> motorTerminals = new HashSet<TerminalInfo>(Arrays.asList(
            motorToControllerTerminalInfo
    ));



    // PartConnectionsInfo
    public static final PartConnectionsInfo controllerConnectionInfo = new PartConnectionsInfo(
            true,
            new HashSet<TerminalInfo>(Arrays.asList(
                    controllerToBatteryTerminalInfo,
                    controllerToMotorTerminalInfo
                    )));

    public static final PartConnectionsInfo batteryConnectionInfo = new PartConnectionsInfo(
            true,
            new HashSet<TerminalInfo>(Arrays.asList(
                    batteryToControllerTerminalInfo
            )));

    public static final PartConnectionsInfo motorConnectionInfo = new PartConnectionsInfo(
            true,
            new HashSet<TerminalInfo>(Arrays.asList(
                    motorToControllerTerminalInfo
            )));
}