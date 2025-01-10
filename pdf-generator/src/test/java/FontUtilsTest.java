import com.alibaba.fastjson2.JSON;
import com.itextpdf.io.font.FontNames;
import com.itextpdf.io.font.TrueTypeFont;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.netease.lowcode.pdf.extension.utils.FontUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.util.Set;

public class FontUtilsTest {
    public static void main1(String[] args) {
//        System.out.println(FontUtils.availableFonts());
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (Font font : ge.getAllFonts()) {
            System.out.println(String.format("name=%s,family=%s",font.getFontName(),font.getFamily()));
        }
    }

    public static void main2(String[] args) throws IOException {
        TrueTypeFont trueTypeFont = new TrueTypeFont("");
    }

    public static void main(String[] args) throws IOException {
        Set<String> registeredFonts = PdfFontFactory.getRegisteredFonts();
//

        int i = PdfFontFactory.registerSystemDirectories();

        for (String font : registeredFonts) {
            if(StringUtils.contains(font,"阿里")){
                System.out.println(font);
                PdfFont registeredFont = PdfFontFactory.createRegisteredFont(font);
                System.out.println(registeredFont);
                FontNames fontNames = registeredFont.getFontProgram().getFontNames();
//                System.out.println(fontNames.getCidFontName());
//                System.out.println(fontNames.getFontName());
//                System.out.println(fontNames.getFontStretch());
                System.out.println(JSON.toJSONString(fontNames));
            }
        }


        Set<String> registeredFamilies = PdfFontFactory.getRegisteredFamilies();
    }
}
