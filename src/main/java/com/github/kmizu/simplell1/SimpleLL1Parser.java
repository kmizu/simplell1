package com.github.kmizu.simplell1;

import sun.java2d.pipe.SpanShapeRenderer;

public class SimpleLL1Parser
{
    public class ParseError extends RuntimeException {
        public ParseError(int position, String message) {
            super("ParseError at: " + position + " message: " + message);
        }
    }
    public static class Input {
        private int current = 0;
        private char[] input;
        public Input(String input) {
            this.input = input.toCharArray();
        }
        public void advance() {
            current++;
        }
        public void consume(char ch) {
            assert input[current] == ch;
            current++;
        }
        public int current() {
            return current;
        }
        public char look() {
            if(current >= input.length) return 0;
            return input[current];
        }
    }

    private Input input;

    public SimpleLL1Parser(String inputFromString) {
        this.input = new Input(inputFromString);
    }

    public void reset(String inputFromString) {
        input = new Input(inputFromString);
    }

    public int expression() {
        return additive();
    }

    private int additive() {
        int result = multitive();
        outer:
        {
            while (true) {
                switch (input.look()) {
                    case '+': {
                        input.consume('+');
                        int rhs = multitive();
                        result += rhs;
                        break;
                    }
                    case '-': {
                        input.consume('-');
                        int rhs = multitive();
                        result -= rhs;
                        break;
                    }
                    default:
                        break outer;
                }
            }
        }
        return result;
    }

    private int multitive() {
        int result = primary();
        outer:
        {
            while (true) {
                switch (input.look()) {
                    case '*': {
                        input.consume('*');
                        int rhs = primary();
                        result *= rhs;
                        break;
                    }
                    case '/': {
                        input.consume('/');
                        int rhs = primary();
                        result /= rhs;
                        break;
                    }
                    default:
                        break outer;
                }
            }
        }
        return result;
    }

    private int primary() {
        switch(input.look()) {
            case '(':
                input.consume('(');
                int result = expression();
                input.consume(')');
                return result;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return number();
            default:
                throw new ParseError(input.current(), "expect [0-9] or (");
        }
    }

    private int number() {
        char ch;
        int result = 0;
        ch = input.look();
        if(!('0' <= ch && ch <= '9')) {
            throw new RuntimeException("Parsing Error At:" + input.current());
        }
        while(true) {
            ch = input.look();
            if(!('0' <= ch && ch <= '9')) break;
            result = result * 10 + (ch - '0');
            input.advance();
        }
        return result;
    }

    public static void main( String[] args )
    {
        System.out.println(new SimpleLL1Parser("(2+2*3)/4").expression());
    }
}
