package com.example.afreecar.model.checklist.assembly;

import com.example.afreecar.model.abstraction.Equatable;

public interface QRDistance<TImpl extends QRDistance<TImpl>> extends Equatable<TImpl> {

    Double getQRDistance();
}
