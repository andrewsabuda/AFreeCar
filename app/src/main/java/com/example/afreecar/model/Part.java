package com.example.afreecar.model;

import java.util.Map;

public class Part {

    private ID id;
    private PartType type;
    private boolean needsMechanicalConnection;

    private Map<PartType, ID> terminalConnections;
}
