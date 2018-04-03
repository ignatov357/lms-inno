package com.awesprojects.innolib.managers;

import android.support.annotation.NonNull;

import com.awesprojects.innolib.InnolibApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by ilya on 2/25/18.
 */

public class SecureStorageManager {

    public static final String TAG = "SecStorageManager";
    private static final Logger log = Logger.getLogger(TAG);

    public static SecureStorageManager mInstance;

    public static SecureStorageManager getInstance() {
        if (mInstance == null)
            mInstance = new SecureStorageManager();
        return mInstance;
    }

    public HashMap<String, String> mData;
    public File mStorage;

    private SecureStorageManager() {
        mStorage = new File(InnolibApplication.getInstance().getFilesDir(), "storage.sec");
        if (!load())
            mData = new HashMap<>();
    }

    public SecureStorageTransaction beginTransaction() {
        return new SecureStorageTransaction(this);
    }

    protected int commit(SecureStorageTransaction transaction) {
        return save();
    }

    public String get(String key) {
        return mData.get(key);
    }


    private int save() {
        try {
            FileOutputStream fos = new FileOutputStream(mStorage);
           // EncryptingOutputStream eos = new EncryptingOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mData);
            oos.close();
            return 0;
        } catch (FileNotFoundException e) {
            log.throwing(this.getClass().getName(), "save", e);
            return 1;
        } catch (IOException e) {
            log.throwing(this.getClass().getName(), "save", e);
            return 2;
        }
    }

    private boolean load() {
        try {
            FileInputStream fis = new FileInputStream(mStorage);
            //EncryptingInputStream eis = new EncryptingInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(fis);
            //noinspection unchecked
            mData = (HashMap<String, String>) ois.readObject();
            return true;
        } catch (FileNotFoundException e) {
            log.throwing(this.getClass().getName(), "save", e);
        } catch (IOException e) {
            log.throwing(this.getClass().getName(), "save", e);
        } catch (ClassNotFoundException e) {
            log.throwing(this.getClass().getName(), "save", e);
        }
        log.warning("secure storage does not exists");
        return false;
    }


    public static class SecureStorageTransaction {
        final SecureStorageManager secureStorageManager;

        SecureStorageTransaction(SecureStorageManager manager) {
            secureStorageManager = manager;
        }

        public SecureStorageTransaction put(String key, String value) {
            secureStorageManager.mData.put(key, value);
            return this;
        }

        public int commit() {
            return secureStorageManager.commit(this);
        }
    }

    private static final class EncryptingOutputStream extends FilterOutputStream {

        public EncryptingOutputStream(@NonNull OutputStream out) {
            super(out);
        }

        @Override
        public void write(int i) throws IOException {
            out.write(i^255);
        }

    }

    private static final class EncryptingInputStream extends FilterInputStream{

        protected EncryptingInputStream(InputStream in) {
            super(in);
        }

        @Override
        public int read() throws IOException {
            int rb = in.read();
            if (rb==-1) return -1;
            return rb^255;
        }
    }
}
