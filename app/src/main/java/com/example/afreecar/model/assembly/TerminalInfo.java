package com.example.afreecar.model.assembly;

import androidx.annotation.NonNull;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.PartTag;

/**
 * Class containing information about a terminal's QR code ID and its target unique part tag.
 */
public class TerminalInfo {

    private ID id;
    private PartTag target;

    public TerminalInfo(@NonNull ID terminalID, @NonNull PartTag targetPartTag) {
        this.id = terminalID;
        this.target = targetPartTag;
    }

    public ID getID() {
        return id;
    }

    public PartTag getTarget() {
        return target;
    }
}
