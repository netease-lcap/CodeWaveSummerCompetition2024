package com.netease.lowcode.pdf.extension.utils;

import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ImageUtils {

    public static Map<String,byte[]> getSheetImages(Workbook wb) {

        Map<String,byte[]> picDataMap = new HashMap<>();

        if(wb instanceof XSSFWorkbook){

            Map<String,byte[]> tmp = new HashMap<>();

            XSSFWorkbook xssfWorkbook = (XSSFWorkbook) wb;

            List<? extends PictureData> allPictures = wb.getAllPictures();
            for (PictureData pictureData : allPictures) {
                // 根据imageName拿到图片
                byte[] data = pictureData.getData();
                XSSFPictureData xssfPictureData = (XSSFPictureData) pictureData;
                PackagePartName partName = xssfPictureData.getPackagePart().getPartName();

                tmp.put(partName.getName(), data);
            }

            List<PackagePart> cellImages = xssfWorkbook.getPackage().getPartsByName(Pattern.compile("/xl/cellimages.xml"));

            for (PackagePart cellImage : cellImages) {

                try {
                    InputStream inputStream = cellImage.getInputStream();
                    Map<String, String> picMap = XmlParse.parse(inputStream);

                    for (Map.Entry<String, String> entry : picMap.entrySet()) {

                        PackageRelationship rId1 = cellImage.getRelationship(entry.getValue());
                        URI targetURI = rId1.getTargetURI();
                        picDataMap.put(entry.getKey(), tmp.get(targetURI.toString()));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return picDataMap;
    }

}
