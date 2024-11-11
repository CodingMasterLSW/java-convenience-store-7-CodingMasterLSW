package store.utils;

import static store.exception.ErrorMessage.FILE_LOAD_EXCEPTION;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileLoader {

    private FileLoader(){
    }

    public static FileLoader create() {
        return new FileLoader();
    }

    public static List<String> loadFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            readLinesFromFile(br, lines);
        } catch (IOException e) {
            throw new IllegalArgumentException(FILE_LOAD_EXCEPTION.getMessage());
        }
        return lines;
    }

    private static void readLinesFromFile(BufferedReader br, List<String> lines) throws IOException {
        br.readLine(); // 헤더 라인 스킵
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
    }

}
