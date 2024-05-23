import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * 替换指定目录下指定文件的部分文本（可用来统一替换作者名称或其他标记）
 *
 * @Description AuthorNameChangeScript
 * @Author Filo
 * @Version 1.0
 */
public class AuthorNameChangeScript {
    public static void main(String[] args) throws IOException {
        final Path dirPath = Paths.get("M:\\WORK_SPACE\\abc-cloud\\abc");
        final String fileSuffix = ".java";

        try (Stream<Path> walk = Files.walk(dirPath)) {
            walk.forEach(path -> {
                try {
                    File file = path.toFile();
                    if (file.isFile() && file.getName().endsWith(fileSuffix)) {
                        Path dstPath = Paths.get(file.getAbsolutePath() + ".modified");
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dstPath.toFile()));
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            line = line.replace("AAA", "[author_name]")
                                    .replace("BBB", "[author_name]")
                                    .replace("@Date 2023/", "@Date 2077/");
                            bufferedWriter.write(line);
                            bufferedWriter.newLine();
                        }
                        bufferedReader.close();
                        bufferedWriter.close();
                        Files.deleteIfExists(path);
                        Files.move(dstPath, path);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            System.out.printf(">>>>>>>>|%s%n", e.getMessage());
            throw e;
        }
    }
}
