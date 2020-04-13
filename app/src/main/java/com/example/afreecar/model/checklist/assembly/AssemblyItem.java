package com.example.afreecar.model.checklist.assembly;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.abstraction.Hashable;

public interface AssemblyItem<T extends AssemblyItem<T>> extends Hashable<T> {

    ID getID();

    Double getQRDistance();
}
