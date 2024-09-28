import java.util.*;

public class ProblemGenerator {
    private final int numProblems;
    private final int range;
    private final Set<String> problems;
    private final FileManager fileManager;
    private final ExpressionEvaluator evaluator;

    public ProblemGenerator(int numProblems, int range) {
        this.numProblems = numProblems;
        this.range = range;
        this.problems = new HashSet<>();
        this.fileManager = new FileManager();
        this.evaluator = new ExpressionEvaluator();
    }

    public void generateProblems() {
        // 在写入文件之前先清空文件内容
        fileManager.clearFile("Exercises.txt");
        fileManager.clearFile("Answers.txt");

        Random random = new Random();
        int problemNumber = 1; // 序号

        while (problems.size() < numProblems) {
            String problem = generateRandomProblem(random);
            if (!problems.contains(problem)) {
                String result = evaluator.evaluate(problem);
                if (result != null && !result.startsWith("-")) {  // 确保结果有效且非负
                    problems.add(problem);
                    String problemWithNumber = problemNumber + ". " + problem + " =";
                    String resultWithNumber = problemNumber + ". " + result;
                    fileManager.writeProblem(problemWithNumber);
                    fileManager.writeAnswer(resultWithNumber);
                    problemNumber++;
                }
            }
        }
    }

    private String generateRandomProblem(Random random) {
        StringBuilder problem = new StringBuilder();
        int numOperators = random.nextInt(3) + 1; // 随机决定运算符的数量（1到3个）

        problem.append(generateSubExpression(random, numOperators));

        return problem.toString();
    }

    private String generateSubExpression(Random random, int numOperators) {
        List<String> operands = new ArrayList<>();
        List<String> operators = new ArrayList<>();

        // 生成第一个操作数并初始化当前结果
        String currentOperand = generateRandomOperand(random);
        double currentResult = evaluateOperand(currentOperand);
        operands.add(currentOperand);

        for (int i = 0; i < numOperators; i++) {
            String operator = generateRandomOperator(random);
            String nextOperand = generateRandomOperand(random);
            double nextValue = evaluateOperand(nextOperand);

            // 调整运算符和操作数以确保不产生负数
            if (operator.equals("-")) {
                if (currentResult < nextValue) {
                    // 如果减法会导致负数，交换操作数和符号
                    operator = "+";
                }
            } else if (operator.equals("/")) {
                if (nextValue == 0 || currentResult < nextValue) {
                    // 确保除数不为零，且避免分子小于分母导致结果小于1
                    operator = "*";
                }
            }

            // 更新当前结果
            currentResult = calculateNewResult(currentResult, nextValue, operator);

            operators.add(operator);
            operands.add(nextOperand);
        }

        // 随机决定是否将某些操作数和运算符包围在括号中，确保括号不包围整个表达式
        if (numOperators > 1) {
            int startIdx = random.nextInt(numOperators); // 括号的起始位置
            int endIdx = startIdx + random.nextInt(numOperators - startIdx) + 1; // 括号的结束位置

            // 确保括号不包围整个表达式
            if (startIdx > 0 || endIdx < numOperators) {
                operands.set(startIdx, "(" + operands.get(startIdx));
                operands.set(endIdx, operands.get(endIdx) + ")");
            }
        }

        // 拼接表达式
        StringBuilder subExpression = new StringBuilder();
        for (int i = 0; i < numOperators; i++) {
            subExpression.append(operands.get(i)).append(" ").append(operators.get(i)).append(" ");
        }
        subExpression.append(operands.get(numOperators));

        return subExpression.toString();
    }

    private double calculateNewResult(double currentResult, double nextValue, String operator) {
        switch (operator) {
            case "+":
                return currentResult + nextValue;
            case "-":
                return currentResult - nextValue;
            case "*":
                return currentResult * nextValue;
            case "/":
                return currentResult / nextValue;
            default:
                return currentResult;
        }
    }

    private double evaluateOperand(String operand) {
        if (operand.contains("'")) {
            // 带分数
            String[] parts = operand.split("'");
            double wholeNumber = Double.parseDouble(parts[0]);
            String[] fraction = parts[1].split("/");
            double numerator = Double.parseDouble(fraction[0]);
            double denominator = Double.parseDouble(fraction[1]);
            return wholeNumber + (numerator / denominator);
        } else if (operand.contains("/")) {
            // 真分数
            String[] fraction = operand.split("/");
            double numerator = Double.parseDouble(fraction[0]);
            double denominator = Double.parseDouble(fraction[1]);
            return numerator / denominator;
        } else {
            // 整数
            return Double.parseDouble(operand);
        }
    }

    private String generateRandomOperand(Random random) {
        int type = random.nextInt(3); // 调整为3种类型：自然数、真分数、带分数
        switch (type) {
            case 0: // 生成自然数
                return String.valueOf(random.nextInt(range) + 1);
            case 1: // 生成真分数并化简
                int numerator, denominator;
                do {
                    numerator = random.nextInt(range - 1) + 1;
                    denominator = random.nextInt(range - 1) + 2; // 确保分母大于分子，且分母最小为2
                } while (numerator >= denominator); // 重复生成直到分子小于分母
                return simplifyFraction(numerator, denominator);
            case 2: // 生成带分数并化简真分数部分
                int wholeNumber = random.nextInt(range - 1) + 1; // 整数部分的最大值由 range 决定
                do {
                    numerator = random.nextInt(range - 1) + 1;
                    denominator = random.nextInt(range - 1) + 2; // 确保分母大于分子，且分母最小为2
                } while (numerator >= denominator); // 重复生成直到分子小于分母
                String simplifiedFraction = simplifyFraction(numerator, denominator);
                return wholeNumber + "'" + simplifiedFraction;
            default:
                return String.valueOf(random.nextInt(range) + 1);
        }
    }

    private String simplifyFraction(int numerator, int denominator) {
        int gcd = gcd(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;
        return numerator + "/" + denominator;
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private String generateRandomOperator(Random random) {
        switch (random.nextInt(4)) {
            case 0:
                return "+";
            case 1:
                return "-";
            case 2:
                return "*";
            case 3:
                return "/"; // 使用“/”来表示除法
            default:
                return "+";
        }
    }
}