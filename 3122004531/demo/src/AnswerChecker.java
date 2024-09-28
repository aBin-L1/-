import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerChecker {
    private final String exerciseFile;
    private final String answerFile;
    private final FileManager fileManager;

    public AnswerChecker(String exerciseFile, String answerFile) {
        this.exerciseFile = exerciseFile;
        this.answerFile = answerFile;
        this.fileManager = new FileManager();
    }

    public void checkAnswers() {
        List<String> exercises = readFile(exerciseFile);
        List<String> answers = readFile(answerFile);

        if (exercises.size() != answers.size()) {
            System.out.println("题目和答案数量不匹配！");
            return;
        }

        List<Integer> correct = new ArrayList<>();
        List<Integer> wrong = new ArrayList<>();

        for (int i = 0; i < exercises.size(); i++) {
            // 去掉题目序号和等号后的部分，保留表达式
            String exerciseExpression = exercises.get(i).substring(exercises.get(i).indexOf(".") + 1).replace("=", "").trim();

            // 计算题目的正确答案
            String correctAnswer = new ExpressionEvaluator().evaluate(exerciseExpression);
            if (correctAnswer == null) {
                System.out.println("Exercise " + (i + 1) + ": Evaluation returned null.");
                wrong.add(i + 1);
                continue;
            }

            // 去掉答案序号和等号后的部分，保留用户提供的答案
            String userAnswer = answers.get(i).substring(answers.get(i).indexOf(".") + 1).trim();

            // 比较用户答案和计算的正确答案
            if (correctAnswer.equals(userAnswer)) {
                correct.add(i + 1);
            } else {
                wrong.add(i + 1);
            }
        }

        // 将结果写入文件
        fileManager.writeGrade(correct, wrong);
    }

    private List<String> readFile(String fileName) {
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

