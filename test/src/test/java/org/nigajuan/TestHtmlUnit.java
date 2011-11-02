package org.nigajuan;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: nigajuan
 * Date: 23/01/11
 * Time: 23:34
 * To change this template use File | Settings | File Templates.
 */
@Test()
public class TestHtmlUnit {

    private static final int NB_THREAD = 10;
    private static final Logger logger = LoggerFactory.getLogger(TestHtmlUnit.class);


    @DataProvider(name = "pages")
    public Object[][] createPageData(){
        return new Object[][]{
                {"primefaces1_0_0" , "http://localhost:8080/primefaces1_0_0/datatablePage.jsf" , "yui-pg0-0-next-link"},
                {"primefaces" , "http://localhost:8080/primefaces/datatablePage.jsf" , "yui-pg0-0-next-link"},

                {"richfaces1_0_0" , "http://localhost:8080/richfaces1_0_0/datatablePage.jsf", "datatableForm:datatableScroller_ds_next"},
                {"richfaces" , "http://localhost:8080/richfaces/datatablePage.jsf", "datatableForm:datatableScroller_ds_next"},

                {"icefaces1_0_0", "http://localhost:8080/icefaces1_0_0/datatablePage.jsf", "datatableForm:j_idt6next"},
                {"icefaces", "http://localhost:8080/icefaces/datatablePage.jsf", "datatableForm:j_idt6next"}
        };
    }

    @DataProvider(name = "bench")
    public Object[][] createBenchData(){
        return new Object[][]{
                {"primefaces1_0_0" , "http://localhost:8080/primefaces1_0_0/datatablePage.jsf" , 400},
                {"primefaces" , "http://localhost:8080/primefaces/datatablePage.jsf" , 400},
                {"richfaces1_0_0" , "http://localhost:8080/richfaces1_0_0/datatablePage.jsf", 400},
                {"richfaces" , "http://localhost:8080/richfaces/datatablePage.jsf", 400},
                {"icefaces1_0_0", "http://localhost:8080/icefaces1_0_0/datatablePage.jsf", 400},
                {"icefaces", "http://localhost:8080/icefaces/datatablePage.jsf", 400}
        };
    }
    
    @Test(singleThreaded = true , dataProvider = "pages")
    public void getPage(String name , String url , String nextId) throws Exception {
        getGenericPage(name, url, nextId);
    }
    

    @Test(singleThreaded = true , dataProvider = "bench")
    public void bench(String name , String url , int iteration) throws Exception {
        MeanCalculator meanCalculator;
        meanCalculator = launchRequestsThread(url, iteration, false);
        logger.info(name + " " + meanCalculator.getSum() + "/" + meanCalculator.getCount() + "=" + meanCalculator.mean());
    }



    private MeanCalculator launchRequestsThread(final String url, final int iteration, final boolean clearCoockies) throws Exception {
        final MeanCalculator meanCalculator = new MeanCalculator();
        ExecutorService service = Executors.newFixedThreadPool(NB_THREAD);
        for (int i = 0; i < NB_THREAD; i++) {
            service.execute(new Thread() {
                @Override
                public void run() {
                    try {
                        requestPage(url, iteration, meanCalculator, clearCoockies);
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

    private void requestPage(String url, int iteration, MeanCalculator meanCalculator , boolean clearCoockies) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
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
                if (clearCoockies){
                    httpclient.getCookieStore().clear();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }


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
        page.getElementsByTagName("span");
        logger.info(pageId + "\t" + sizeResponseListener.toString());
    }


}
