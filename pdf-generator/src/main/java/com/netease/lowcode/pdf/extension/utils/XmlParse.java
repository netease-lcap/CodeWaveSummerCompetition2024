package com.netease.lowcode.pdf.extension.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class XmlParse {

    public static Map<String, String> parse(InputStream inputStream) {

        Map<String, String> picMap = new HashMap<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            // etc:cellImages
            Element root = document.getDocumentElement();

            NodeList childNodes = root.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                // etc:cellImage
                Node etc_cellImage = childNodes.item(i);
                Node xdr_pic = getChildNodeByName(etc_cellImage, "xdr:pic");
                Node xdr_nvPicPr = getChildNodeByName(xdr_pic, "xdr:nvPicPr");
                Node xdr_cNvPr = getChildNodeByName(xdr_nvPicPr, "xdr:cNvPr");
                String name = xdr_cNvPr.getAttributes().getNamedItem("name").getTextContent();

                Node xdr_blipFill = getChildNodeByName(xdr_pic, "xdr:blipFill");
                Node a_blip = getChildNodeByName(xdr_blipFill, "a:blip");
                String rId = a_blip.getAttributes().getNamedItem("r:embed").getTextContent();

                picMap.put(name, rId);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        return picMap;
    }

    private static Node getChildNodeByName(Node node, String name) {
        if (Objects.isNull(node)) {
            return null;
        }
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (StringUtils.equals(item.getNodeName(), name)) {
                return item;
            }
        }
        return null;
    }

    private static void getNode(Node node, List<String> tagList, Integer index) {
        if (Objects.isNull(node) || CollectionUtils.isEmpty(tagList) || Objects.isNull(index) || index >= tagList.size()) {
            return;
        }

        if (StringUtils.equals(node.getNodeName(), tagList.get(index))) {
            if (index + 1 == tagList.size()) {
                Node name = node.getAttributes().getNamedItem("name");
                System.out.println(name);
                return;
            }
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                getNode(item, tagList, index + 1);
            }
        }

        return;
    }
}
