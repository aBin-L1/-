public class Main {
    public static void main(String[] args) {
        // 参数解析
        if (args.length == 0) {
            showUsage();
            return;
        }

        try {
            if (args[0].equals("-n") && args.length >= 2 && args[2].equals("-r")) {
                int numProblems = Integer.parseInt(args[1]);
                int range = Integer.parseInt(args[3]);
                ProblemGenerator generator = new ProblemGenerator(numProblems, range);
                generator.generateProblems();
            } else if (args[0].equals("-e") && args[2].equals("-a")) {
                String exerciseFile = args[1];
                String answerFile = args[3];
                AnswerChecker checker = new AnswerChecker(exerciseFile, answerFile);
                checker.checkAnswers();
            } else {
                showUsage();
            }
        } catch (NumberFormatException e) {
            System.out.println("参数格式错误，请输入正确的数字格式。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showUsage() {
        System.out.println("Usage:");
        System.out.println("生成题目: Myapp.exe -n <题目数量> -r <数值范围>");
        System.out.println("检查答案: Myapp.exe -e <题目文件> -a <答案文件>");
    }
}
