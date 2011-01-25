package org.nigajuan;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: nigajuan
 * Date: 23/01/11
 * Time: 23:34
 * To change this template use File | Settings | File Templates.
 */
public class TestHtmlUnit {

    private static final int NB_THREAD = 10;
    private static final Logger logger = LoggerFactory.getLogger(TestHtmlUnit.class);


    private void getGenericPage(String pageId, String url, String nextId) throws Exception {
        WebClient webClient = new WebClient();

        MyWebConnection myWebConnection = new MyWebConnection(webClient);

        SizeResponseListener sizeResponseListener = new SizeResponseListener();
        myWebConnection.setListener(sizeResponseListener);

        webClient.setWebConnection(myWebConnection);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        HtmlPage page = webClient.getPage(url);
        logger.info(pageId + "\t" + sizeResponseListener.toString());

        sizeResponseListener.reset();
        page.getElementById(nextId).click();
        logger.info(pageId + "\t" + sizeResponseListener.toString());
    }

    @Test
    public void getPrimefacesPage() throws Exception {
        getGenericPage("primefaces", "http://localhost:8080/primefaces/datatablePage.jsf", "yui-pg0-0-next-link");
    }

    @Test
    public void getRichfacesPage() throws Exception {
        getGenericPage("richfaces", "http://localhost:8080/richfaces/datatablePage.jsf", "datatableForm:datatableScroller_ds_next");
    }

    @Test
    public void getIcefacesPage() throws Exception {
        getGenericPage("icefaces", "http://localhost:8080/icefaces/datatablePage.jsf", "datatableForm:j_idt6next");
    }


    @Test
    public void benchPrimefaces() throws Exception {
        final MeanCalculator meanCalculator = launchRequestsThread("http://localhost:8080/primefaces/datatablePage.jsf", 400);
        logger.info("benchPrimefaces " + meanCalculator.getSum() + "/" + meanCalculator.getCount() + "=" + meanCalculator.mean());
    }

    @Test(dependsOnMethods = "benchPrimefaces")
    public void benchIcefaces() throws Exception {
        final MeanCalculator meanCalculator = launchRequestsThread("http://localhost:8080/icefaces/datatablePage.jsf", 400);
        logger.info("benchIcefaces " + meanCalculator.getSum() + "/" + meanCalculator.getCount() + "=" + meanCalculator.mean());
    }

    @Test(dependsOnMethods = "benchIcefaces")
    public void benchRichfaces() throws Exception {
        final MeanCalculator meanCalculator = launchRequestsThread("http://localhost:8080/richfaces/datatablePage.jsf", 400);
        logger.info("benchRichfaces " + meanCalculator.getSum() + "/" + meanCalculator.getCount() + "=" + meanCalculator.mean());
    }


    private MeanCalculator launchRequestsThread(final String url, final int iteration) throws Exception {
        final MeanCalculator meanCalculator = new MeanCalculator();
        ExecutorService service = Executors.newFixedThreadPool(NB_THREAD);
        for (int i = 0; i < NB_THREAD; i++) {
            service.execute(new Thread() {
                @Override
                public void run() {
                    try {
                        requestPage(url, iteration, meanCalculator);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            });
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);
        return meanCalculator;
    }

    private void requestPage(String url, int iteration, MeanCalculator meanCalculator) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            for (int i = 0; i < iteration; i++) {
                long start = System.nanoTime();
                HttpGet httpget = new HttpGet(url);
                ResponseHandler<byte[]> responseHandler = new ResponseHandler<byte[]>() {
                    @Override
                    public byte[] handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                        return null;
                    }
                };
                httpclient.execute(httpget, responseHandler);
                float duration = (float) (System.nanoTime() - start) * 1e-9f;
                meanCalculator.add(duration);
            }
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

}
