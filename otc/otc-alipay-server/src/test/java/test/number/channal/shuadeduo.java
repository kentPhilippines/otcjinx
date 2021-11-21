package test.number.channal;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class shuadeduo {
    /*
       static  String  year = DatePattern.PURE_DATE_FORMAT.format(new Date());
        static  String hour  = "7";
        static String minute = "15";
        static  String currencycode = "currency";
        static  String partner = "partner";
        static  String includeSub = "includeSub";
        static   String transac = "transactionType";
        static  String includeSubPartn = "includeSubPartn";
        static  String gameNa = "gameNa";
        static  String gameSuit = "gameSuit";
        static  String Devicetype = "Devicetype";
        static  String profile = "profile"; */
    public static void main(String[] args) throws TransformerException, ParserConfigurationException {
        String year = "";
        String hour = "";
        String minute = "";
        String currencycode = "";
        String partner = "";
        String includeSub = "";
        String transac = "";
        String includeSubPartn = "";
        String gameNa = "";
        String gameSuit = "";
        String devicetype = "";
        String profile = "";
        String xml = xml(year, hour, minute, currencycode, partner, includeSub, transac, includeSubPartn, gameNa, gameSuit, devicetype, profile);
    }


    static String xml(String year, String hour, String minute, String currencycode, String partner, String includeSub,
                      String transac, String includeSubPartn, String gameNa, String gameSuit,
                      String devicetype, String profile) throws ParserConfigurationException, TransformerException {
        //创建xml文件。DOM方式
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("searchdetail");
        root.setAttribute("requestId", UUID.randomUUID().toString());
        Element daterange = document.createElement("daterange");
        Element account = document.createElement("account");
        account.setAttribute("currency", currencycode);
        Element partenr = document.createElement("partenr");
        Element transaction = document.createElement("transaction");
        Element game = document.createElement("game");
        daterange.setAttribute("startDate", year);
        daterange.setAttribute("startDatehour", hour);
        daterange.setAttribute("startMinute", minute);
        partenr.setAttribute("partnerId", partner);
        partenr.setAttribute("includeSubPartner", includeSub);
        transaction.setAttribute("transactionType", transac);
        transaction.setAttribute("includeSubPartner", includeSubPartn);
        game.setAttribute("gameName", gameNa);
        game.setAttribute("gameSuite", gameSuit);
        game.setAttribute("gameDevicetype", devicetype);
        game.setAttribute("gameProfile", profile);
        root.appendChild(daterange);
        root.appendChild(account);
        root.appendChild(partenr);
        root.appendChild(transaction);
        root.appendChild(game);
        document.appendChild(root);//最后一步
        //文档内容创建完毕，下面是输出文档
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer1 = transformerFactory.newTransformer();
        StringWriter writer = new StringWriter();
        transformer1.transform(new DOMSource(document), new StreamResult(writer));
        System.out.println(writer.toString());//成功输出，可以复制
        return writer.toString();
    }




    void  rqXml(String xml){





    }

}
