package org.nigajuan;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Created by IntelliJ IDEA.
 * User: nigajuan
 * Date: 02/11/11
 * Time: 23:43
 * To change this template use File | Settings | File Templates.
 */
public class ElementFinderById implements IElementFinder{

    private final String id;
    
    public ElementFinderById(String id){
        this.id = id;
    }
    
    @Override
    public HtmlElement match(HtmlPage doc) {
        return doc.getElementById(id);
    }
}
