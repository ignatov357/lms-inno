package com.awesprojects.innolib.managers;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Message;

import com.awesprojects.innolib.utils.SignInHandler;
import com.awesprojects.lmsclient.api.Response;
import com.awesprojects.lmsclient.api.UsersAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.internal.Responsable;

import java.util.logging.Logger;

/**
 * Created by ilya on 2/23/18.
 */

public class SignInManager {

    public static final String TAG = "SignInManager";
    private static final Logger log = Logger.getLogger(TAG);

    private static SignInManager mInstance;

    public static SignInManager getInstance() {
        if (mInstance==null)
            mInstance = new SignInManager();
        return mInstance;
    }

    private NfcManager mNfcManager;
    private FingerprintManager mFingerprintManager;
    private Thread mSigningInThread;

    public SignInManager(){

    }

    public void ensureManagersInitialized(Context context){
        if (mNfcManager==null)
            mNfcManager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        if (mFingerprintManager==null && Build.VERSION.SDK_INT>=23)
            mFingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
    }

    public boolean isDeviceSupportsNfc(Context context){
        ensureManagersInitialized(context);
        return mNfcManager.getDefaultAdapter() != null;
    }

    public boolean isDeviceSupportsFingerprint(Context context) {
        ensureManagersInitialized(context);
        return Build.VERSION.SDK_INT >= 23 && mFingerprintManager.isHardwareDetected();
    }

}
