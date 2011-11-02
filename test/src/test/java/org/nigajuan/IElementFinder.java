package org.nigajuan;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Created by IntelliJ IDEA.
 * User: nigajuan
 * Date: 02/11/11
 * Time: 23:41
 * To change this template use File | Settings | File Templates.
 */
public interface IElementFinder {

    public HtmlElement match(HtmlPage doc);
}
