package fr.catcore.gamegui.codec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CodecToStringParser {

    private static final Logger LOGGER = LogManager.getLogger();

    private String mapCodecToString;

    private CodecToStringParser(String mapCodecToString) {
        this.mapCodecToString = mapCodecToString;

        handle();
    }

    public static void parse(String mapCodecToString) {
        if (!mapCodecToString.startsWith("RecordCodec")) {
            cantHandle(mapCodecToString);
            return;
        }

        mapCodecToString = mapCodecToString.substring("RecordCodec".length());
        if (!mapCodecToString.startsWith("[")) {
            cantHandle(mapCodecToString);
            return;
        }
        new CodecToStringParser(mapCodecToString);

//        LOGGER.info(mapCodecToString);
    }

    private void handle() {
        if (this.mapCodecToString.startsWith("[")) {
            int toGo = 0;
            boolean first = true;
            int i = 0;
            while (i < this.mapCodecToString.length()) {
                if (toGo < 1 && !first) {
                    break;
                }
                if (this.mapCodecToString.charAt(i) == '[') toGo++;
                else if (this.mapCodecToString.charAt(i) == ']') toGo--;

                first = false;
                i++;
            }
            String array = this.mapCodecToString.substring(1, i - 1);
            LOGGER.info(array);

            LOGGER.info("Parsed Array:");
            for (String part: this.seperateArray(array)) {
                LOGGER.info(" - " + part);
            }
        }
    }

    private String[] seperateArray(String array) {
        List<String> parts = new ArrayList<>();

        String current = "";
        int currentPos = 0;
        int depth = 0;
        while (currentPos < array.length()) {
            if (array.charAt(currentPos) == '*' && depth == 0) {
                parts.add(current.trim());
                current = "";
            } else {
                current += array.charAt(currentPos);
                if (array.charAt(currentPos) == '[') depth++;
                else if (array.charAt(currentPos) == ']') depth--;
            }

            currentPos++;
        }

        if (!current.isEmpty()) parts.add(current.trim());

        return parts.toArray(new String[0]);
    }

    private static void cantHandle(String str) {
        LOGGER.error("Can't handle Codec: " + str);
    }
}
