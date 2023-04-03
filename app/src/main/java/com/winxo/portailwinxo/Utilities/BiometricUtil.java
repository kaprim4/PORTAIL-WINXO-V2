package com.winxo.portailwinxo.Utilities;

import android.content.Context;

import androidx.biometric.BiometricManager;

public class BiometricUtil {
    public int hasBiometricCapability(Context context) {
        BiometricManager biometricManager = BiometricManager.from(context);
        return biometricManager.canAuthenticate();
    }
}
