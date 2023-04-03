package com.winxo.portailwinxo.Model;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMax implements InputFilter {

    private final Double max;

    public InputFilterMax(Double max) {
        this.max = max;
    }

    public InputFilterMax(String max) {
        this.max = Double.parseDouble(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String replacement = source.subSequence(start, end).toString();
            String newVal = dest.toString().substring(0, dstart) + replacement +dest.toString().substring(dend, dest.toString().length());
            double input = Double.parseDouble(newVal);
            if (input <= max)
                return null;
        } catch (NumberFormatException ignored) {

        }
        return "";
    }
}
