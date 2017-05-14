package com.pingan.http.framework;

import android.content.Context;

import com.pingan.http.R;
import com.pingan.http.framework.network.OkhttpUtil;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class BaseConfig {
    private static Context baseContext;

    public static void init(Context context) {
        baseContext = context;
        OkhttpUtil.init(buildSSLSocketFactory(context, R.raw.geotrust));
    }

    private static SSLSocketFactory buildSSLSocketFactory(Context context, int certRawResId) {
        KeyStore keyStore = null;
        try {
            keyStore = buildKeyStore(context, certRawResId);
        } catch (KeyStoreException e) {
        } catch (CertificateException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (IOException e) {
        }

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

        } catch (NoSuchAlgorithmException e) {
        } catch (KeyStoreException e) {
        }

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
        }
        try {
            sslContext.init(null, tmf.getTrustManagers(), null);
        } catch (KeyManagementException e) {
        }

        return sslContext.getSocketFactory();

    }

    private static KeyStore buildKeyStore(Context context, int certRawResId)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        InputStream inputStream = context.getResources().openRawResource(certRawResId);
        keyStore.load(inputStream, "pw12306".toCharArray());
        return keyStore;
    }

    public static Context getAppContext() {
        return baseContext;
    }
}
