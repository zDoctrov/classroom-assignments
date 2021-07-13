package sample;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import java.util.Stack;

public class Controller {
    @FXML
    private TextField txt_result;                   //Use txt_result.getText() to return the string in the calculator

    Stack<Double> calculatorStack = new Stack<>();  //Where all the operands will be stored before being operated on

    //click numbers 0-9
    public void addNumber (ActionEvent ae)
    {
        if(txt_result.getText().contains("Error"))  //Let's the user immediately type in numbers after an error
        {
            calculatorStack.clear();
            txt_result.clear();
        }

        String number = ((Button)ae.getSource()).getText();
        txt_result.setText(txt_result.getText() + number);
    }

    //click '+','-','*', or '/'
    public void addOperation (ActionEvent ae)
    {
        if(txt_result.getText().contains("Error"))  //Let's the user immediately type in operations after an error
        {                                           // (Even if it will also result in an error)
            calculatorStack.clear();
            txt_result.clear();
        }

        String operation = ((Button)ae.getSource()).getText();
        txt_result.setText(txt_result.getText() + operation);
    }

    //click "C" to make the current string empty
    public void clearCalculator (ActionEvent ae)
    {
        calculatorStack.clear();
        txt_result.setText("");
    }

    //click '_' to add a space
    public void addSpace (ActionEvent ae)
    {
        String space = ((Button)ae.getSource()).getText();
        txt_result.setText(txt_result.getText() + " ");
    }

    // Use this for testing purposes: 10 6 2 + 3 - /
    public void calculateValue (ActionEvent ae)
    {
        String postFixEquation = txt_result.getText();
        String operandsAndOperations[] = postFixEquation.split(" ");  //Ignore whitespace and get entered numbers and symbols

        for(int i = 0; i < operandsAndOperations.length; i++)               //Process all of the symbols one at a time, left to right
        {
            if(operandsAndOperations[i].length() == 1)                      //All operations must be one character long
            {
                if(operandsAndOperations[i].charAt(0) == '+')
                {
                    if(calculatorStack.size() < 2)                          //Prevent unintended stack empty error
                    {
                        System.out.println("Error: No Operands/Use Post-fix Notation");
                        calculatorStack.clear();

                        txt_result.setText("Error: Notation");
                        return;
                    }

                    Double sum;                                             //Pop twice, and then add these values together

                    Double firstPoppedDouble;
                    firstPoppedDouble = calculatorStack.pop();

                    Double secondPoppedDouble;
                    secondPoppedDouble = calculatorStack.pop();

                    sum = secondPoppedDouble + firstPoppedDouble;

                    calculatorStack.push(sum);

                    System.out.println(calculatorStack.peek());
                }
                else if(operandsAndOperations[i].charAt(0) == '*')
                {
                    if(calculatorStack.size() < 2)                          //Prevent unintended stack empty error
                    {
                        System.out.println("Error: No Operands/Use Post-fix Notation");
                        calculatorStack.clear();

                        txt_result.setText("Error: Notation");
                        return;
                    }

                    Double product;                                         //Pop twice, and then multiply these values together

                    Double firstPoppedDouble;
                    firstPoppedDouble = calculatorStack.pop();

                    Double secondPoppedDouble;
                    secondPoppedDouble = calculatorStack.pop();

                    product = secondPoppedDouble * firstPoppedDouble;

                    calculatorStack.push(product);

                    System.out.println(calculatorStack.peek());
                }
                else if(operandsAndOperations[i].charAt(0) == '-')
                {
                    if(calculatorStack.size() < 2)                          //Prevent unintended stack empty error
                    {
                        System.out.println("Error: No Operands/Use Post-fix Notation");
                        calculatorStack.clear();

                        txt_result.setText("Error: Notation");
                        return;
                    }

                    Double difference;                                      //Pop twice, and then subtract one value from the other

                    Double firstPoppedDouble;
                    firstPoppedDouble = calculatorStack.pop();

                    Double secondPoppedDouble;
                    secondPoppedDouble = calculatorStack.pop();

                    difference = secondPoppedDouble - firstPoppedDouble;

                    calculatorStack.push(difference);

                    System.out.println(calculatorStack.peek());
                }
                else if(operandsAndOperations[i].charAt(0) == '/')
                {
                    if(calculatorStack.size() < 2)                          //Prevent unintended stack empty error
                    {
                        System.out.println("Error: No Operands/Use Post-fix Notation");
                        calculatorStack.clear();

                        txt_result.setText("Error: Notation");
                        return;
                    }

                    Double quotient;                                        //Pop twice, and then divide one value from the other

                    Double firstPoppedDouble;
                    firstPoppedDouble = calculatorStack.pop();

                    Double secondPoppedDouble;
                    secondPoppedDouble = calculatorStack.pop();

                    if (firstPoppedDouble == 0)                             //Prevent User from dividing by zero
                    {
                        System.out.println("Error: Can't divide by zero");
                        calculatorStack.clear();

                        txt_result.setText("Error: Zero Division");
                        return;
                    }
                    else
                    {
                        quotient = secondPoppedDouble / firstPoppedDouble;
                        calculatorStack.push(quotient);
                        System.out.println(calculatorStack.peek());
                    }
                }
                else if(Character.isDigit(operandsAndOperations[i].charAt(0)))  //Is a single digit operand
                {
                    Double operandValue;
                    operandValue = Double.parseDouble(operandsAndOperations[i]);

                    calculatorStack.push(operandValue);
                    System.out.println(calculatorStack.peek());
                }
                else                                                            //NOT an operation or a single digit
                {                                                               //(This error is impossible to reach without
                    System.out.println("Error: Unknown symbol entered");        //a keyboard, but just to be safe I left it in)
                    calculatorStack.clear();

                    txt_result.setText("Error: Unknown Symbol");
                    return;
                }

            }
            else                                                                //A operand may be multi-char string
            {
                if(!operandsAndOperations[i].contains("+")                      //Prevent +,*, and / symbols from being a part of an equation
                    && !operandsAndOperations[i].contains("/") && !operandsAndOperations[i].contains("*"))
                {
                    if (operandsAndOperations[i].matches("[0-9]+") || operandsAndOperations[i].contains(".")
                           && operandsAndOperations[i].length() > 1 && Character.isDigit(operandsAndOperations[i].charAt(0)))
                    {   //Only integers or decimal containing double values containing more than one symbol are processed here
                        Double operandValue;
                        operandValue = Double.parseDouble(operandsAndOperations[i]);

                        calculatorStack.push(operandValue);
                        System.out.println(calculatorStack.peek());
                    }
                    else if(operandsAndOperations[i].charAt(0) == '-')          //Negative Numbers need to be processed, despite the '-' showing up
                    {
                        Double operandValue;
                        operandValue = Double.parseDouble(operandsAndOperations[i]);

                        calculatorStack.push(operandValue);
                        System.out.println(calculatorStack.peek());
                    }
                    else
                    {
                        System.out.println("Error: Make sure to use spaces between operands & operations(1)");
                        calculatorStack.clear();

                        txt_result.setText("Error: Notation");
                        return;
                    }
                }
                else
                {
                    System.out.println("Error: Make sure to use spaces between operands & operations(2)");
                    calculatorStack.clear();

                    txt_result.setText("Error: Spacing");
                    return;
                }
            }
        }

        txt_result.setText(Double.toString(calculatorStack.peek())); //Put the String version of the calculated result
        calculatorStack.clear();    //Only have to calculate what's currently on screen, so reset the stack when done
    }
}
