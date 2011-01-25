package org.nigajuan;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * Created by IntelliJ IDEA.
 * User: nigajuan
 * Date: 24/01/11
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public interface IResponseListener {

    public void event(WebRequest request , WebResponse response);
}
