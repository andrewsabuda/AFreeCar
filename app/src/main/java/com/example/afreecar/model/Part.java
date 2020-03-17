package com.example.afreecar.model;

import java.util.Map;

public class Part {

    PartType type;
    Map<PartType, ID> terminalConnections;
    boolean mechanicalConnection;
}
