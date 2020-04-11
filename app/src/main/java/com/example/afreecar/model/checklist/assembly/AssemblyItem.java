package com.example.afreecar.model.checklist.assembly;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.abstraction.Equatable;

public interface AssemblyItem<T extends AssemblyItem<T>> extends Equatable<T> {

    ID getID();

    Double getQRDistance();
}
