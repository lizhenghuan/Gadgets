package cn.robtoium;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

public class WriteXML {
	 // 句柄
    private TransformerHandler handler = null;
    // 输出流
    private OutputStream outStream = null;
    // 根节点
    private String rootElement;

    public WriteXML(String fileName, String rootElement) throws Exception {
        this.rootElement = rootElement;
        // 创建句柄，并设置初始参数
        SAXTransformerFactory fac = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        handler = fac.newTransformerHandler();
        Transformer transformer = handler.getTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        outStream = new FileOutputStream(fileName);
        handler.setResult(new StreamResult(outStream));
        System.out.println("初始化XML成功");
    }

    public void start() throws Exception {
        handler.startDocument();
        handler.startElement("", "", rootElement, null);
        System.out.println("XML开始...");
    }
    
    //这个函数是最重要的，它可以控制各个细节：是否写入属性值，文本值又是多少等等
    public void write(HashMap<String, String> map, String element)
            throws SAXException {
        handler.startElement("", "", element, null);
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            String value = map.get(key);
            handler.startElement("", "", key, null);
            handler.characters(value.toCharArray(), 0, value.length());
            handler.endElement("", "", key);
        }
        handler.endElement("", "", element);
//        System.out.println("写入元素成功！");
    }

    public void end() throws Exception {
        handler.endElement("", "", rootElement);
        handler.endDocument();
        outStream.close();
        System.out.println("XML结束！");
    }
}
