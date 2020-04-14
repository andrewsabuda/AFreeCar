package com.example.afreecar.model;

import androidx.annotation.NonNull;
import javax.annotation.Nullable;

public interface OptionalPair<TSingle> {

    Boolean isPair();

    @NonNull TSingle getOne();

    @Nullable TSingle getTwo();
}
