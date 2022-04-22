//package calculator;

import java.math.BigInteger;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, String> variables = new HashMap<>();

        while (true) {

            String str = scanner.nextLine();
            if (str.equals("/exit")) {
                System.out.println("Bye!");
                break;
            } else if (str.equals("/help")) {
                System.out.println("This is a smart calculator.");
            } else if (str.matches("/.*")) {
                System.out.println("Unknown command");
            } else {
                if (!str.isEmpty()) {
                    if (str.contains("=")) {
                        str = str.replaceAll("\\s+", "");
                        String[] vars = str.split("=");
                        if (vars.length != 2) {
                            System.out.println("Invalid assignment");
                        } else if (!vars[1].matches("[+-]*\\d+")) {
                            if (variables.containsKey(vars[1])) {
                                variables.put(vars[0], variables.get(vars[1]));
                            } else {
                                System.out.println("Invalid assignment");
                            }
                        } else if (!vars[0].matches("[a-zA-Z]+")) {
                            System.out.println("Invalid identifier");
                        } else {
                            variables.put(vars[0], vars[1]);
                        }
                    } else if (str.contains(" ")) {

                        if ((str.contains("(") && !str.contains(")")) || (!str.contains("(") && str.contains(")"))) {
                            System.out.println("Invalid expression");
                        } else if (str.contains("**") || str.contains("//")) {
                            System.out.println("Invalid expression");
                        }
                        else {
                            String[] vars = infixToPostfix(str).split("\\s+");
                            for (int i = 0; i < vars.length; i++) {
                                if (vars[i].matches("[a-zA-Z]+")) {
                                    if (variables.containsKey(vars[i])) {
                                        vars[i] = variables.get(vars[i]);
                                    } else {
                                        System.out.println("Unknown variable");
                                    }
                                }
                            }
                            System.out.println(process(vars));
                        }

                    } else {
                        if (str.matches("\\d+")) {
                            System.out.println(str);
                        } else {
                            System.out.println(variables.getOrDefault(str, "Unknown variable"));
                        }
                    }
                }
            }
        }
    }

    private static BigInteger process(String[] nums) {
        BigInteger result = BigInteger.ZERO;

        ArrayList<String> vars = new ArrayList<>(Arrays.asList(nums));

        while (true) {

            for (int i = 0; i < vars.size(); i++) {
                if (vars.get(i).matches("[+]+")) {
                    BigInteger a = new BigInteger(vars.get(i - 1));
                    BigInteger b = new BigInteger(vars.get(i - 2));
                    result = result.add(a.add(b));
                    vars.remove(i);
                    vars.remove(i - 1);
                    vars.remove(i - 2);
                    vars.add(i - 2, result.toString());
                    result = BigInteger.ZERO;
                    break;
                } else if (vars.get(i).matches("[-]+")) {
                    if (vars.get(i).length() % 2 == 0) {
                        BigInteger a = new BigInteger(vars.get(i - 1));
                        BigInteger b = new BigInteger(vars.get(i - 2));
                        result = result.add(a.add(b));
                        vars.remove(i);
                        vars.remove(i - 1);
                        vars.remove(i - 2);
                        vars.add(i - 2, result.toString());
                        result = BigInteger.ZERO;
                        break;
                    } else {
                        BigInteger a = new BigInteger(vars.get(i - 1));
                        BigInteger b = new BigInteger(vars.get(i - 2));
                        result = result.add(b.subtract(a));
                        vars.remove(i);
                        vars.remove(i - 1);
                        vars.remove(i - 2);
                        vars.add(i - 2, result.toString());
                        result = BigInteger.ZERO;
                        break;
                    }
                } else if (vars.get(i).matches("[*]")) {
                    BigInteger a = new BigInteger(vars.get(i - 1));
                    BigInteger b = new BigInteger(vars.get(i - 2));
                    result = result.add(a.multiply(b));
                    vars.remove(i);
                    vars.remove(i - 1);
                    vars.remove(i - 2);
                    vars.add(i - 2, result.toString());
                    result = BigInteger.ZERO;
                    break;
                } else if (vars.get(i).matches("[/]")) {
                    BigInteger a = new BigInteger(vars.get(i - 1));
                    BigInteger b = new BigInteger(vars.get(i - 2));
                    result = result.add(b.divide(a));
                    vars.remove(i);
                    vars.remove(i - 1);
                    vars.remove(i - 2);
                    vars.add(i - 2, result.toString());
                    result = BigInteger.ZERO;
                    break;
                }
            }

            if (vars.size() == 1) {
                result = new BigInteger(vars.get(0));
                break;
            }

        }

        return result;

    }

    private static int Prec(String ch)
    {
        switch (ch)
        {
            case "+":
            case "-":
                return 1;

            case "*":
            case "/":
                return 2;

            case "^":
                return 3;
        }
        return -1;
    }

    static String infixToPostfix(String exp)
    {
        // initializing empty String for result
        String result = new String("");

        // initializing empty stack
        Stack<String> stack = new Stack<>();

        if (exp.contains("(")) {
            exp = exp.replace("(", "( ");
        }
        if (exp.contains(")")) {
            exp = exp.replace(")", " )");
        }

        String[] vars = exp.split("\\s+");

        for (String c : vars) {
            // If the scanned character is an
            // operand, add it to output.
            if (c.matches("[A-Za-z0-9]+"))
                result += c + " ";

                // If the scanned character is an '(',
                // push it to the stack.
            else if (c.equals("("))
                stack.push(c);

                //  If the scanned character is an ')',
                // pop and output from the stack
                // until an '(' is encountered.
            else if (c.equals(")")) {
                while (!stack.isEmpty() &&
                        !stack.peek().equals("("))
                    result += stack.pop() + " ";

                stack.pop();
            } else // an operator is encountered
            {
                while (!stack.isEmpty() && Prec(c)
                        <= Prec(stack.peek())) {

                    result += stack.pop() + " ";
                }
                stack.push(c);
            }

        }

        // pop all the operators from the stack
        while (!stack.isEmpty()){
            if(stack.peek().equals("("))
                return "Invalid Expression";
            result += stack.pop() + " ";
        }
        return result;
    }

}
