import java.util.Stack;

public class ExpressionEvaluator {

    public String evaluate(String expression) {
        try {
            // 将表达式转化为后缀表达式
            String postfix = infixToPostfix(expression);
            // 计算后缀表达式的值
            String result = evaluatePostfix(postfix);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    private String infixToPostfix(String infix) {
        // 将中缀表达式转化为后缀表达式
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();

        for (char c : infix.toCharArray()) {
            if (Character.isDigit(c) || c == '/' || c == '\'') {
                postfix.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(' ').append(stack.pop());
                }
                stack.pop();
            } else if (isOperator(c)) {
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                    postfix.append(' ').append(stack.pop());
                }
                postfix.append(' ');
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            postfix.append(' ').append(stack.pop());
        }

        return postfix.toString();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private int precedence(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    private String evaluatePostfix(String postfix) {
        // 计算后缀表达式的值
        Stack<Fraction> stack = new Stack<>();
        String[] tokens = postfix.split(" ");

        for (String token : tokens) {
            if (isOperator(token.charAt(0))) {
                Fraction b = stack.pop();
                Fraction a = stack.pop();
                stack.push(calculate(a, b, token.charAt(0)));
            } else {
                stack.push(Fraction.fromString(token));
            }
        }

        return stack.pop().toString();
    }

    private Fraction calculate(Fraction a, Fraction b, char operator) {
        switch (operator) {
            case '+':
                return a.add(b);
            case '-':
                return a.subtract(b);
            case '*':
                return a.multiply(b);
            case '/':
                return a.divide(b);
            default:
                return new Fraction(0, 1);
        }
    }
}
