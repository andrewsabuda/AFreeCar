package com.example.afreecar.model.checklist.assembly;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.abstraction.Hashable;

public interface AssemblyItem<TImpl extends AssemblyItem<TImpl>> extends QRDistance<TImpl> {

    ID getID();
}
