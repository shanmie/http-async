package cn.org.http.async.ssl;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * @author deacon
 * @since 2019/8/26
 */
@Slf4j
public class SSLThrough {
    private static final SSLHandler simpleVerifier = new SSLHandler();
    private static SSLSocketFactory sslFactory;
    private static SSLConnectionSocketFactory sslConnFactory;
    private static SSLIOSessionStrategy sslIOSessionStrategy;
    private static SSLThrough sslThrough = new SSLThrough();
    private SSLContext sc;

    public static SSLThrough getInstance() {
        return sslThrough;
    }

    public static SSLThrough custom() {
        return new SSLThrough();
    }

    // 重写X509TrustManager类的三个方法,信任服务器证书
    private static class SSLHandler implements X509TrustManager, HostnameVerifier {

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
            //return null;
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }

        @Override
        public boolean verify(String paramString, SSLSession paramSSLSession) {
            return true;
        }
    }

    ;

    // 信任主机
    public static HostnameVerifier getVerifier() {
        return simpleVerifier;
    }

    public synchronized SSLSocketFactory getSSLSF(SSLProtocolVersion sslpv) {
        if (sslFactory != null)
            return sslFactory;
        try {
            SSLContext sc = getSSLContext(sslpv);
            sc.init(null, new TrustManager[]{simpleVerifier}, null);
            sslFactory = sc.getSocketFactory();
        } catch (KeyManagementException e) {
            log.error(e.getMessage(), e);
        }
        return sslFactory;
    }

    public synchronized SSLConnectionSocketFactory getSSLConnSf(SSLProtocolVersion sslPv) {
        if (sslConnFactory != null)
            return sslConnFactory;
        try {
            SSLContext sc = getSSLContext(sslPv);
//	    	sc.init(null, new TrustManager[] { simpleVerifier }, null);
            sc.init(null, new TrustManager[]{simpleVerifier}, new java.security.SecureRandom());
            sslConnFactory = new SSLConnectionSocketFactory(sc, simpleVerifier);
        } catch (KeyManagementException e) {
            log.error(e.getMessage(), e);
        }
        return sslConnFactory;
    }

    public synchronized SSLIOSessionStrategy getSSLIos(SSLProtocolVersion sslPv) {
        if (sslIOSessionStrategy != null)
            return sslIOSessionStrategy;
        try {
            SSLContext sc = getSSLContext(sslPv);
//			sc.init(null, new TrustManager[] { simpleVerifier }, null);
            sc.init(null, new TrustManager[]{simpleVerifier}, new java.security.SecureRandom());
            sslIOSessionStrategy = new SSLIOSessionStrategy(sc, simpleVerifier);
        } catch (KeyManagementException e) {
            log.error(e.getMessage(), e);
        }
        return sslIOSessionStrategy;
    }

    public SSLThrough customSSL(String keyStorePath, String keyStorepass) {
        FileInputStream in = null;
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            in = new FileInputStream(new File(keyStorePath));
            trustStore.load(in, keyStorepass.toCharArray());
            // 相信自己的CA和所有自签名的证书
            sc = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                assert in != null;
                in.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return this;
    }

    public SSLContext getSSLContext(SSLProtocolVersion sslpv) {
        try {
            if (sc == null) {
                sc = SSLContext.getInstance(sslpv.getName());
            }
            return sc;
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return sc;
    }

    /**
     * The SSL protocol version (SSLv3, TLSv1, TLSv1.1, TLSv1.2)
     *
     * @author arron
     * @version 1.0
     */
    public enum SSLProtocolVersion {
        SSL("SSL"),
        SSLv3("SSLv3"),
        TLSv1("TLSv1"),
        TLSv1_1("TLSv1.1"),
        TLSv1_2("TLSv1.2"),
        ;
        private String name;

        SSLProtocolVersion(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static SSLProtocolVersion find(String name) {
            for (SSLProtocolVersion pv : SSLProtocolVersion.values()) {
                if (pv.getName().toUpperCase().equals(name.toUpperCase())) {
                    return pv;
                }
            }
            throw new RuntimeException("未支持当前ssl版本号：" + name);
        }

    }
}
