import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String EXERCISE_FILE = "Exercises.txt";
    private static final String ANSWER_FILE = "Answers.txt";
    private static final String GRADE_FILE = "Grade.txt";

    // 新增方法用于清空指定文件
    public void clearFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // 不写入任何内容，达到清空文件的目的
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeProblem(String problem) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EXERCISE_FILE, true))) {
            writer.write(problem + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeAnswer(String answer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ANSWER_FILE, true))) {
            writer.write(answer + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeGrade(List<Integer> correct, List<Integer> wrong) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GRADE_FILE))) {
            writer.write("Correct: " + correct.size() + " " + correct.toString().replace("[", "(").replace("]", ")") + "\n");
            writer.write("Wrong: " + wrong.size() + " " + wrong.toString().replace("[", "(").replace("]", ")") + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printFileContent(String fileName) {
        List<String> lines = readFile(fileName);
        for (String line : lines) {
            System.out.println(line);
        }
    }

    // 读取文件内容的方法
    public List<String> readFile(String fileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
