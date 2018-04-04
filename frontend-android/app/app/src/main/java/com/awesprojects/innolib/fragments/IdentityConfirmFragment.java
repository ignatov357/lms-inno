package com.awesprojects.innolib.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awesprojects.innolib.InnolibApplication;
import com.awesprojects.innolib.R;
import com.awesprojects.innolib.managers.SecureStorageManager;
import com.awesprojects.innolib.widgets.PinFieldView;
import com.awesprojects.innolib.widgets.PinKeyboardView;

import java.io.IOException;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Created by ilya on 2/23/18.
 */

public class IdentityConfirmFragment extends AbstractExtendedFragment
        implements PinKeyboardView.PinKeyboardCallback, View.OnClickListener{

    LinearLayout mContent;
    Callback mCallback;
    PinFieldView mPinView;
    PinKeyboardView mPinKeyboard;
    TextView mInfoTextView;
    Button mRightButton;
    boolean trainMode;
    Runnable mOnPinEntered;
    int[] mLastReceivedPin;
    byte[] mLastCardData;
    Runnable mOnPinConfirmed;
    Runnable mOnPinMismatch;
    Runnable mOnLeftKeyboardAction;
    Runnable mOnRightKeyboardAction;
    Runnable mOnRightButtonClick;
    Runnable mAfterCardRead;
    Runnable mOnFingerprintError;
    Runnable mOnFingerprintSuccess;
    NfcDataReader mNfcDataReader;

    boolean cardEnabled;
    boolean fingerprintEnabled;

    FingerprintHelper mFingerprintHelper;
    FingerprintHandler mFingerprintHandler;

    public byte[] getUserCardData(){
        return mLastCardData;
    }

    public int[] getPin(){
        return mLastReceivedPin;
    }

    @Override
    public void onKeyboardAction(int action) {
        if (action==PinKeyboardView.ACTION_LEFT){
            if (mOnLeftKeyboardAction!=null)
                mOnLeftKeyboardAction.run();
        }else{
            if (mOnRightKeyboardAction!=null)
                mOnRightKeyboardAction.run();
        }
    }

    public boolean equalPin(int[] pin1,int[] pin2){
        if (pin1.length!=pin2.length) return false;
        for (int i = 0; i < pin1.length; i++) {
            if (pin1[i]!=pin2[i]) return false;
        }
        return true;
    }

    @Override
    public void onKeyboardKey(int number) {
        mPinView.addPinNumber(number);
        if (mPinView.getPinLength()==4){
            int[] pin = mPinView.getPin();
            if (trainMode && mLastReceivedPin!=null){
                if (equalPin(pin,mLastReceivedPin)){
                    mLastReceivedPin = mPinView.getPin();
                    if (mOnPinConfirmed!=null)
                        mOnPinConfirmed.run();
                }else{
                    if (mOnPinMismatch!=null)
                        mOnPinMismatch.run();
                }
            }else {
                mLastReceivedPin = mPinView.getPin();
                if (mOnPinEntered!=null)
                    mOnPinEntered.run();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view==mRightButton){
            if (mOnRightButtonClick!=null)
                mOnRightButtonClick.run();
        }
    }

    public void onNfcDataReceived(byte[] bytes){
        mLastCardData = bytes;
        if (mAfterCardRead!=null)
            mAfterCardRead.run();
    }

    public void onFingerprintError(){
        if (mOnFingerprintError!=null)
            mOnFingerprintError.run();
    }

    public void onFingerprintSuccess(){
        if (mOnFingerprintSuccess!=null)
            mOnFingerprintSuccess.run();
    }

    public interface Callback{
        void onIdentityConfirmSuccess();
        void onIdentityConfirmFail();
    }

    public void setCallbackListener(Callback callback){
        mCallback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_identity_confirm);
        mContent = (LinearLayout) getContentView();
        mPinView = mContent.findViewById(R.id.fragment_identity_confirm_pin_field_view);
        mPinKeyboard = mContent.findViewById(R.id.fragment_identity_confirm_keyboard_view);
        mPinKeyboard.setKeyboardCallback(this);
        mInfoTextView = mContent.findViewById(R.id.fragment_identity_confirm_tutorial_textview);
        mRightButton = mContent.findViewById(R.id.fragment_identity_confirm_right_button);
        mRightButton.setOnClickListener(this);
        mFingerprintHelper = new FingerprintHelper(this);
        mFingerprintHandler = new FingerprintHandler(this);
        SharedPreferences sp = getActivity().getSharedPreferences(InnolibApplication.PREFERENCES_SIGNIN_METHODS, Context.MODE_PRIVATE);
        cardEnabled = sp.getBoolean("sign_in_using_card",false);
        fingerprintEnabled = sp.getBoolean("sign_in_using_fingerprint",false);
        if (trainMode){
            mInfoTextView.setVisibility(View.VISIBLE);
            startTrain();
        }else{
            startVerification();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return mContent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setTrainMode(boolean train){
        trainMode = train;
    }

    //pin start
    public void startTrain(){
       // boolean pinEnabled = sp.getBoolean("sign_in_using_pin",false);
        if (cardEnabled || fingerprintEnabled){
            mInfoTextView.setText(R.string.identity_confirm_train_pin_info);
        }else{
            mInfoTextView.setText(R.string.identity_confirm_train_pin_only_info);
        }
        mPinKeyboard.setActionImage(PinKeyboardView.ACTION_RIGHT,R.drawable.ic_backspace_black_24dp);
        mOnPinEntered = () -> continueTrain1();
    }

    //pin confirm
    public void continueTrain1(){
        if (cardEnabled | fingerprintEnabled)
            mInfoTextView.setText(R.string.identity_confirm_train_pin_repeat_info);
        else
            mInfoTextView.setText(R.string.identity_confirm_train_pin_only_repeat_info);
        mPinView.clearPin();
        mOnPinConfirmed = () -> {
            continueTrain2();
        };
        mOnPinMismatch = () -> {
            mPinView.clearPin();
            mInfoTextView.setText(R.string.identity_confirm_pin_mismatch);
        };
    }

    //branch - fingerprint/card
    public void continueTrain2(){
        TransitionManager.beginDelayedTransition(mContent);
        mPinView.setVisibility(View.INVISIBLE);
        mPinKeyboard.setVisibility(View.INVISIBLE);
        if (cardEnabled | fingerprintEnabled){
            if (fingerprintEnabled){
                continueTrain3();
            }else{
                continueTrain5();
            }
        }else{
            saveAllAndConfirm();
        }
    }

    //fingerprint
    public void continueTrain3(){
        int availableNow = mFingerprintHelper.isFingerprintReadyToUse();
        switch (availableNow){
            case 0:{
                mInfoTextView.setText(R.string.identity_confirm_train_fingerprint_default);
                mRightButton.setVisibility(View.VISIBLE);
                if (cardEnabled)
                    mRightButton.setText(R.string.identity_confirm_train_fingerprint_next_to_card);
                else
                    mRightButton.setText(R.string.identity_confirm_train_fingerprint_finish);
                mOnRightButtonClick = () -> continueTrain4();
            }
        }
    }

    //branch
    public void continueTrain4(){
        if (cardEnabled){
            continueTrain5();
        }else{
           saveAllAndConfirm();
        }
    }

    //card
    public void continueTrain5(){
        mAfterCardRead = () -> continueTrain6();
        TransitionManager.beginDelayedTransition(mContent);
        mInfoTextView.setText(R.string.identity_confirm_train_card_read_ready);
        mRightButton.setVisibility(View.INVISIBLE);
        startCardReading();
    }

    //card confirm
    public void continueTrain6(){
        TransitionManager.beginDelayedTransition(mContent);
        mRightButton.setText(R.string.identity_confirm_train_card_finish);
        mRightButton.setVisibility(View.VISIBLE);
        mInfoTextView.setText(R.string.identity_confirm_train_card_read_done);
        mOnRightButtonClick = () -> {
            saveAllAndConfirm();
        };
    }



    public void saveAllAndConfirm(){
        SecureStorageManager.SecureStorageTransaction secureStorageTransaction = SecureStorageManager.getInstance()
                .beginTransaction();
        StringBuilder pin = new StringBuilder();
        int[] pinArray = mLastReceivedPin;
        for (int i = 0; i < pinArray.length; i++) {
            pin.append(pinArray[i]).append("");
        }
        secureStorageTransaction.put("PIN",pin.toString());
        if (cardEnabled){
            StringBuilder card = new StringBuilder();
            byte[] cardArray = mLastCardData;
            for (int i = 0; i < cardArray.length; i++) {
                card.append(Integer.toHexString((int)cardArray[i]));
            }
            secureStorageTransaction.put("CARD",card.toString());
        }
        secureStorageTransaction.commit();
        confirm(true);
    }

    public void confirm(boolean isConfirmed){
        mInfoTextView.setVisibility(View.INVISIBLE);
        if (mCallback!=null) {
            if (isConfirmed)
                mCallback.onIdentityConfirmSuccess();
            else
                mCallback.onIdentityConfirmFail();
        }
    }




    public void startVerification(){
        if (Build.VERSION.SDK_INT>=23 && fingerprintEnabled) {
            int isReadyToUse = mFingerprintHelper.isFingerprintReadyToUse();
            if (isReadyToUse == 0) {
                mFingerprintHelper.generateKey();
                boolean init = mFingerprintHelper.initCipher();
                if (init) {
                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(
                            mFingerprintHelper.getCipher());
                    mFingerprintHandler.startAuth(mFingerprintHelper.mFingerprintManager, cryptoObject);
                    mOnFingerprintSuccess = () -> confirm(true);;
                }
            }
        }
        if (cardEnabled){
            mAfterCardRead = () -> {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mLastCardData.length; i++) {
                    sb.append(Integer.toHexString(mLastCardData[i]));
                }
                String card = SecureStorageManager.getInstance().get("CARD");
                if (card.equalsIgnoreCase(sb.toString()))
                    confirm(true);
            };
        }
        mPinKeyboard.setActionImage(PinKeyboardView.ACTION_RIGHT,R.drawable.ic_backspace_black_24dp);
        mOnRightKeyboardAction = () -> {
            mPinView.removeLastPinNumber();
        };
        mOnPinEntered = () -> {
            mPinKeyboard.setEnabled(false);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mLastReceivedPin.length; i++) {
                sb.append(mLastReceivedPin[i]);
            }
            String pin = SecureStorageManager.getInstance().get("PIN");
            if (pin.equalsIgnoreCase(sb.toString())) {
                Fade fadeIn = new Fade(Fade.IN);
                fadeIn.setDuration(100);
                TransitionManager.beginDelayedTransition(mContent,fadeIn);
                mInfoTextView.setText("");
                mContent.findViewById(R.id.fragment_identity_indeterminate_progress_container)
                        .setVisibility(View.VISIBLE);
                confirm(true);
            }else{
                mPinView.clearPin();
                mPinView.startWrongTypeAnimation();
                TransitionManager.beginDelayedTransition(mContent);
                mInfoTextView.setText(R.string.identity_confirm_pin_mismatch);
                mInfoTextView.setVisibility(View.VISIBLE);
                mPinKeyboard.setEnabled(true);
            }
        };
        mOnPinConfirmed = mOnPinEntered;
    }

    public void startCardReading(){
        Activity activity = getActivity();
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
        if (nfc != null) {
            nfc.enableReaderMode(activity, mNfcDataReader, READER_FLAGS, null);
        }
        cardReading = true;
    }

    public void stopCardReading(){
        Activity activity = getActivity();
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            nfc.disableReaderMode(activity);
        }
        cardReading = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cardReading){
            startCardReading();
        }
    }

    boolean cardReading = false;

    @Override
    public void onPause() {
        super.onPause();
        if (cardReading) {
            stopCardReading();
            cardReading = true;
        }
    }

    public static class FingerprintHelper{

        final String mKeyName;
        final IdentityConfirmFragment mIdentityConfirmFragment;
        KeyguardManager mKeyguardManager;
        FingerprintManager mFingerprintManager;
        KeyStore mKeyStore;
        KeyGenerator mKeyGenerator;
        Cipher mCipher;

        public FingerprintHelper(IdentityConfirmFragment identityConfirmFragment){
            mKeyName = "InnoLibKey";
            mIdentityConfirmFragment = identityConfirmFragment;
            mKeyguardManager = (KeyguardManager) mIdentityConfirmFragment.getActivity()
                    .getSystemService(Context.KEYGUARD_SERVICE);
            mFingerprintManager = (FingerprintManager) mIdentityConfirmFragment.getActivity()
                    .getSystemService(Context.FINGERPRINT_SERVICE);
        }

        public Cipher getCipher(){
            return mCipher;
        }

        public int isFingerprintReadyToUse(){
            if (Build.VERSION.SDK_INT >= 23) {
                if (true) return 0;
                if (!mFingerprintManager.isHardwareDetected())
                    return 1;
                if (mIdentityConfirmFragment.getActivity().checkSelfPermission(Manifest.permission.USE_FINGERPRINT)
                        != PackageManager.PERMISSION_GRANTED)
                    return 2;
                if (!mFingerprintManager.hasEnrolledFingerprints())
                    return 3;
                return 0;
            }else{
                return -1;
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        public void generateKey(){
            try {
                mKeyStore = KeyStore.getInstance("AndroidKeyStore");
                mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                mKeyStore.load(null);
                mKeyGenerator.init(new
                        KeyGenParameterSpec.Builder(mKeyName,
                        KeyProperties.PURPOSE_ENCRYPT |
                                KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());
                mKeyGenerator.generateKey();
            } catch (Throwable t){
                t.printStackTrace();
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        public boolean initCipher() {
            try {
                mCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" +
                        KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            } catch (Throwable t) {
                throw new RuntimeException("Failed to get Cipher", t);
            }
            try {
                mKeyStore.load(null);
                SecretKey key = (SecretKey) mKeyStore.getKey(mKeyName, null);
                mCipher.init(Cipher.ENCRYPT_MODE, key);
                return true;
            } catch (KeyPermanentlyInvalidatedException e) {
                return false;
            } catch (Throwable e) {
                throw new RuntimeException("Failed to init Cipher", e);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static class FingerprintHandler extends FingerprintManager.AuthenticationCallback{

        final IdentityConfirmFragment identityConfirmFragment;
        CancellationSignal cancellationSignal;

        public FingerprintHandler(IdentityConfirmFragment identityConfirmFragment){
            this.identityConfirmFragment = identityConfirmFragment;
        }

        public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject){
            cancellationSignal = new CancellationSignal();
            manager.authenticate(cryptoObject,cancellationSignal,0,this,null);
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
        }
        @Override
        public void onAuthenticationFailed() {
            identityConfirmFragment.onFingerprintError();
        }
        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {}
        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            identityConfirmFragment.onFingerprintSuccess();
        }
    }

    public static class NfcDataReader implements NfcAdapter.ReaderCallback{

        final IdentityConfirmFragment identityConfirmFragment;

        public NfcDataReader(IdentityConfirmFragment identityConfirmFragment){
            this.identityConfirmFragment = identityConfirmFragment;
        }

        public void onDataReceived(byte[] data){
            identityConfirmFragment.onNfcDataReceived(data);
        }

        @Override
        public void onTagDiscovered(Tag tag) {
            Ndef ndef = Ndef.get(tag);
            try {
                byte[] bytes = ndef.getNdefMessage().toByteArray();
                byte[] usefulData = new byte[64];
                System.arraycopy(bytes, 0, usefulData, 0, 64);
                onDataReceived(usefulData);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FormatException e) {
                e.printStackTrace();
            }
        }

    }

}
