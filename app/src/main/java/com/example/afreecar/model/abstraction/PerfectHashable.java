package com.example.afreecar.model.abstraction;

public interface PerfectHashable<TImpl extends PerfectHashable<TImpl>> extends Comparable<TImpl>, Hashable<TImpl> {

}
