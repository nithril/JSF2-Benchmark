package org.nigajuan;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * Created by IntelliJ IDEA.
 * User: nigajuan
 * Date: 24/01/11
 * Time: 21:47
 * To change this template use File | Settings | File Templates.
 */
public class SizeResponseListener implements IResponseListener {

    private long html = 0;
    private long js = 0;
    private long css = 0;
    private long total = 0;

    @Override
    public void event(WebRequest request, WebResponse response) {
        if (request.getUrl().getPath().contains(".js.jsf")) {
            js += response.getContentAsString().length();
        } else if (request.getUrl().getPath().contains(".css.jsf")) {
            css += response.getContentAsString().length();
        } else if (request.getUrl().getPath().contains(".jsf")) {
            html += response.getContentAsString().length();
        } else if (request.getUrl().getPath().contains(".js")) {
            js += response.getContentAsString().length();
        } else if (request.getUrl().getPath().contains(".htm")) {
            html += response.getContentAsString().length();
        } else if (request.getUrl().getPath().contains(".css")) {
            css += response.getContentAsString().length();
        }
        total += response.getContentAsString().length();
    }

    public void reset() {
        html = 0;
        js = 0;
        css = 0;
        total = 0;
    }

    public long getHtml() {
        return html;
    }

    public long getJs() {
        return js;
    }

    public long getCss() {
        return css;
    }

    public long getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "HTML:" + getHtml() + "\tJS:" + getJs() + "\tCSS:" +
                       getCss() + "\tTotal:" + getTotal();
    }
}
