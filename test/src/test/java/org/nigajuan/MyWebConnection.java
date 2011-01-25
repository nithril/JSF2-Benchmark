package org.nigajuan;

import com.gargoylesoftware.htmlunit.HttpWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: nigajuan
 * Date: 23/01/11
 * Time: 23:37
 * To change this template use File | Settings | File Templates.
 */
public class MyWebConnection extends HttpWebConnection {

    private IResponseListener listener = null;

    public MyWebConnection(WebClient client){
        super(client);
    }

    @Override
    public WebResponse getResponse(WebRequest request) throws IOException {
        WebResponse response =  super.getResponse(request);
        if (listener != null){
            listener.event(request, response);
        }
        return response;
    }

    public IResponseListener getListener() {
        return listener;
    }

    public void setListener(IResponseListener listener) {
        this.listener = listener;
    }
}
