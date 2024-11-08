package store.utils;

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
            br.readLine(); // 헤더 라인 스킵
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] 파일을 읽는 중 오류가 발생했습니다.", e);
        }
        return lines;
    }

}
