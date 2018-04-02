package com.awesprojects.innolib.managers;

import com.awesprojects.innolib.InnolibApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
}
