/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package studio.dreamys.font;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Fonts {

    @FontDetails(fontName = "Roboto Medium", fontSize = 35)
    public static GameFontRenderer font35;

    @FontDetails(fontName = "Roboto Medium", fontSize = 40)
    public static GameFontRenderer font40;

    @FontDetails(fontName = "Roboto Bold", fontSize = 180)
    public static GameFontRenderer fontBold180;

    @FontDetails(fontName = "Minecraft Font")
    public static final FontRenderer minecraftFont = Minecraft.getMinecraft().fontRendererObj;

    private static final List<GameFontRenderer> CUSTOM_FONT_RENDERERS = new ArrayList<>();

    public static void loadFonts() {
        long l = System.currentTimeMillis();

        System.out.println("Loading Fonts.");

//        downloadFonts();

        font35 = new GameFontRenderer(getFont("Roboto-Medium.ttf", 35));
        font40 = new GameFontRenderer(getFont("Roboto-Medium.ttf", 40));
        fontBold180 = new GameFontRenderer(getFont("Roboto-Bold.ttf", 180));

        try {
            CUSTOM_FONT_RENDERERS.clear();

            File fontsFile = new File("/assets/near/fonts.json");

            if (fontsFile.exists()) {
                JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(fontsFile)));

                if (jsonElement instanceof JsonNull)
                    return;

                JsonArray jsonArray = (JsonArray) jsonElement;

                for (JsonElement element : jsonArray) {
                    if (element instanceof JsonNull)
                        return;

                    JsonObject fontObject = (JsonObject) element;

                    Font font = getFont(fontObject.get("fontFile").getAsString(), fontObject.get("fontSize").getAsInt());

                    CUSTOM_FONT_RENDERERS.add(new GameFontRenderer(getFont(fontObject.get("fontFile").getAsString(), fontObject.get("fontSize").getAsInt())));
                }
            } else {
                fontsFile.createNewFile();

                PrintWriter printWriter = new PrintWriter(new FileWriter(fontsFile));
                printWriter.println(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonArray()));
                printWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Loaded Fonts. (" + (System.currentTimeMillis() - l) + "ms)");
    }


//    public static IFontRenderer getFontRenderer(String name, int size) {
//        for (Field field : Fonts.class.getDeclaredFields()) {
//            try {
//                field.setAccessible(true);
//
//                Object o = field.get(null);
//
//                if (o instanceof IFontRenderer) {
//                    FontDetails fontDetails = field.getAnnotation(FontDetails.class);
//
//                    if (fontDetails.fontName().equals(name) && fontDetails.fontSize() == size)
//                        return (IFontRenderer) o;
//                }
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return CUSTOM_FONT_RENDERERS.getOrDefault(new FontInfo(name, size), minecraftFont);
//    }

//    public static FontInfo getFontDetails(IFontRenderer fontRenderer) {
//        for (Field field : Fonts.class.getDeclaredFields()) {
//            try {
//                field.setAccessible(true);
//
//                Object o = field.get(null);
//
//                if (o.equals(fontRenderer)) {
//                    FontDetails fontDetails = field.getAnnotation(FontDetails.class);
//
//                    return new FontInfo(fontDetails.fontName(), fontDetails.fontSize());
//                }
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (Map.Entry<FontInfo, IFontRenderer> entry : CUSTOM_FONT_RENDERERS.entrySet()) {
//            if (entry.getValue() == fontRenderer)
//                return entry.getKey();
//        }
//
//        return null;
//    }

//    public static List<IFontRenderer> getFonts() {
//        List<IFontRenderer> fonts = new ArrayList<>();
//
//        for (Field fontField : Fonts.class.getDeclaredFields()) {
//            try {
//                fontField.setAccessible(true);
//
//                Object fontObj = fontField.get(null);
//
//                if (fontObj instanceof IFontRenderer) fonts.add((IFontRenderer) fontObj);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//
//        fonts.addAll(Fonts.CUSTOM_FONT_RENDERERS.values());
//
//        return fonts;
//    }

    private static Font getFont(String fontName, int size) {
        try {
            InputStream inputStream = new FileInputStream(new File("/assets/near/", fontName));
            Font awtClientFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtClientFont = awtClientFont.deriveFont(Font.PLAIN, size);
            inputStream.close();
            return awtClientFont;
        } catch (Exception e) {
            e.printStackTrace();

            return new Font("default", Font.PLAIN, size);
        }
    }

    private static void extractZip(String zipFile, String outputFolder) {
        byte[] buffer = new byte[1024];

        try {
            File folder = new File(outputFolder);

            if (!folder.exists()) folder.mkdir();

            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));

            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(outputFolder + File.separator + zipEntry.getName());
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fileOutputStream = new FileOutputStream(newFile);

                int i;
                while ((i = zipInputStream.read(buffer)) > 0)
                    fileOutputStream.write(buffer, 0, i);

                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }

            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class FontInfo {
        private final String name;
        private final int fontSize;

        public FontInfo(String name, int fontSize) {
            this.name = name;
            this.fontSize = fontSize;
        }

        public FontInfo(Font font) {
            this(font.getName(), font.getSize());
        }

        public String getName() {
            return name;
        }

        public int getFontSize() {
            return fontSize;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FontInfo fontInfo = (FontInfo) o;

            if (fontSize != fontInfo.fontSize) return false;
            return Objects.equals(name, fontInfo.name);
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + fontSize;
            return result;
        }
    }

}