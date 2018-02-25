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

/**
 * Created by ilya on 2/23/18.
 */

public class SignInManager {

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
        if (mFingerprintManager==null)
            mFingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
    }

    public boolean isDeviceSupportsNfc(Context context){
        ensureManagersInitialized(context);
        if (true) return true;
        return mNfcManager.getDefaultAdapter()!=null ? true : false;
    }

    public boolean isDeviceSupportsFingerprint(Context context){
        ensureManagersInitialized(context);
        if (true) return true;
        if (Build.VERSION.SDK_INT>=23)
            return mFingerprintManager.isHardwareDetected();
        return false;
    }

    public void startApiSigningIn(String id_str,String password){
        int id = 0;
        try{
            id = Integer.parseInt(id_str);
        }catch(Throwable t){
            t.printStackTrace();
            mHandler.sendEmptyMessage(-1);
            return;
        }
        final int final_id = id;
        mSigningInThread = new Thread(()->{
            Responsable responsable = UsersAPI.getAccessToken(final_id,password);
            if (responsable instanceof AccessToken){
                Message m = new Message();
                m.what = 200;
                m.obj = responsable;
                mHandler.sendMessage(m);
            }else if(responsable instanceof Response){
                Message m = new Message();
                m.what = ((Response) responsable).getStatus();
                m.obj = responsable;
                mHandler.sendMessage(m);
            }
            mSigningInThread = null;
        });
        mSigningInThread.start();
    }

    private SignInHandler mHandler = new SignInHandler();

    public SignInHandler getSignInHandler(){
        return mHandler;
    }

}
