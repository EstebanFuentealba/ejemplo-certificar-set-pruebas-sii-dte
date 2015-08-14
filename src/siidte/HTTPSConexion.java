/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siidte;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManagerFactory;

/**
 *
 * @author koala
 */
public class HTTPSConexion {

    private HTTPMethod method;
    private CookieManager cookieManager;
    private URL url;
    private HttpsURLConnection httpConnection;
    private String urlCertifPXF;
    private String passCert;
    private String postVars;
    private String cookiePrefix;
    private boolean isCookies = false;
    public HTTPSConexion() {
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        this.cookieManager = new CookieManager();
    }

    public HTTPSConexion(String url, HTTPMethod metod, String params, String urlCertifPXF, String passCert, boolean cookie) throws MalformedURLException, ProtocolException, IOException, NoSuchMethodException, MalformedURLException, IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        this();
        this.url = new URL(url);
        this.method = metod;
        this.postVars = params;
        this.urlCertifPXF = urlCertifPXF;
        this.passCert = passCert;
        this.isCookies = cookie;
        httpConnection = (HttpsURLConnection) this.url.openConnection();
        this.addHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US) AppleWebKit/534.3 (KHTML, like Gecko) Chrome/6.0.472.63 Safari/534.3");
        httpConnection.setSSLSocketFactory(this.getSSLContext().getSocketFactory());


        if (this.method == HTTPMethod.GET) {
            httpConnection.setRequestMethod("GET");
        } else if (this.method == HTTPMethod.POST) {
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            this.addHeader("Content-Type", "application/x-www-form-urlencoded");
        }
        if (this.isCookies) {
            HTTPSConexion c = new HTTPSConexion(url, urlCertifPXF, passCert);
            c.setCookies();
            this.cookieManager = c.cookieManager;
        }
    }

    public void setCookies() throws IOException {
        this.cookieManager.storeCookies(httpConnection);
    }

    public String getCookies() {
        return this.cookieManager.toString() + " " + this.cookiePrefix;
    }

    public HTTPSConexion(String url, String urlCertifPXF, String passCert) throws MalformedURLException, ProtocolException, IOException, NoSuchMethodException, MalformedURLException, IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        this(url, HTTPMethod.GET, "", urlCertifPXF, passCert, false);
    }

    public HTTPSConexion(String url, String urlCertifPXF, String passCert, boolean cookie) throws MalformedURLException, ProtocolException, IOException, NoSuchMethodException, MalformedURLException, IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        this(url, HTTPMethod.GET, "", urlCertifPXF, passCert, cookie);
    }
    private void download() {
        if(!isCookies) {
            this.addHeader("Cookie", this.getCookies());
        }
    }
    public String downloadAsFile(String fileName) throws IOException {
        this.download();
        if (this.method == HTTPMethod.POST) {
            getHttpConnection().getOutputStream().write(this.getPostVars().getBytes("UTF-8"));
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(getHttpConnection().getInputStream());

            bos = new BufferedOutputStream(new FileOutputStream(fileName));

            int i;
            while ((i = bis.read()) != -1) {
                bos.write(i);
            }
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return fileName;
    }
    
    public String downloadAsString() throws IOException {
        this.download();
        if (this.method == HTTPMethod.POST) {
            getHttpConnection().getOutputStream().write(this.getPostVars().getBytes("UTF-8"));
        }
        StringBuilder sb = new StringBuilder();
        InputStream in = getHttpConnection().getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public void addHeader(String key, String value) {
        getHttpConnection().addRequestProperty(key, value);
    }

    private SSLContext getSSLContext() throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("SSL");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(getUrlCertifPXF()), getPassCert().toCharArray());
        kmf.init(ks, getPassCert().toCharArray());
        String alias = ks.aliases().nextElement();

        X509Certificate x509 = (X509Certificate) ks.getCertificate(alias);
        context.init(kmf.getKeyManagers(), null, null);
        return context;
    }

    /**
     * @return the method
     */
    public HTTPMethod getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(HTTPMethod method) {
        this.method = method;
    }

    /**
     * @return the cookieManager
     */
    public CookieManager getCookieManager() {
        return cookieManager;
    }

    /**
     * @param cookieManager the cookieManager to set
     */
    public void setCookieManager(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    /**
     * @return the url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(URL url) {
        this.url = url;
    }

    /**
     * @return the httpConnection
     */
    public HttpsURLConnection getHttpConnection() {
        return httpConnection;
    }

    /**
     * @param httpConnection the httpConnection to set
     */
    public void setHttpConnection(HttpsURLConnection httpConnection) {
        this.httpConnection = httpConnection;
    }

    /**
     * @return the urlCertifPXF
     */
    public String getUrlCertifPXF() {
        return urlCertifPXF;
    }

    /**
     * @param urlCertifPXF the urlCertifPXF to set
     */
    public void setUrlCertifPXF(String urlCertifPXF) {
        this.urlCertifPXF = urlCertifPXF;
    }

    /**
     * @return the passCert
     */
    public String getPassCert() {
        return passCert;
    }

    /**
     * @param passCert the passCert to set
     */
    public void setPassCert(String passCert) {
        this.passCert = passCert;
    }

    /**
     * @return the postVars
     */
    public String getPostVars() {
        return postVars;
    }

    /**
     * @param postVars the postVars to set
     */
    public void setPostVars(String postVars) {
        this.postVars = postVars;
    }

    /**
     * @return the cookiePrefix
     */
    public String getCookiePrefix() {
        return cookiePrefix;
    }

    /**
     * @param cookiePrefix the cookiePrefix to set
     */
    public void setCookiePrefix(String cookiePrefix) {
        this.cookiePrefix = cookiePrefix;
    }
}
