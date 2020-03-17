package com.example.afreecar.model;

import com.example.afreecar.model.identification.PartRequirement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class containing static instances of other objects to be used for demo/testing purposes.
 */
public class DemoStuff {

    // IDs
    public static final ID kitID = new ID("0");
    public static final ID controllerID = new ID("1");
    public static final ID batteryID = new ID("2");
    public static final ID motorID = new ID("3");

    // PartsRequirements
    public static final PartRequirement controllerReq = new PartRequirement(new PartTag(PartType.Controller, 1), new HashSet<ID>(Arrays.asList(controllerID)));
    public static final PartRequirement batteryReq = new PartRequirement(new PartTag(PartType.Battery, 1), new HashSet<ID>(Arrays.asList(batteryID)));
    public static final PartRequirement motorReq = new PartRequirement(new PartTag(PartType.Motor, 1), new HashSet<ID>(Arrays.asList(motorID)));

}