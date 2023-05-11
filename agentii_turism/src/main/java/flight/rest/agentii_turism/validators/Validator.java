package flight.rest.agentii_turism.validators;

import flight.rest.agentii_turism.exceptions.InvalidFieldException;
import flight.rest.agentii_turism.exceptions.Errors;

import java.util.List;
import java.util.Objects;

public class Validator {
    public void checkEmpty(String word) throws InvalidFieldException {
        if(word.isEmpty())
            throw new InvalidFieldException(Errors.emptyField);
    }

    public void checkOnlyLetters(String name) throws InvalidFieldException {
        checkEmpty(name);
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if(!Character.isLetter(c)) {
                throw new InvalidFieldException(Errors.nameError);
            }
        }
    }
    public void checkName(String name) throws InvalidFieldException {
        checkEmpty(name);
        String[] nameParts = name.split(" ");
        for(String n: nameParts)
            checkOnlyLetters(n);
    }
    public  void checkPasswords(String password, String repeatPassword) throws InvalidFieldException {
        checkEmpty(password);
        checkEmpty(repeatPassword);
        if(!Objects.equals(password, repeatPassword))
            throw new InvalidFieldException(Errors.passwordsError);
    }

    public void checkUserRegister(String name, String username, String password, String repeatPassword) throws InvalidFieldException {
        checkOnlyLetters(name);
        checkEmpty(username);
        checkPasswords(password, repeatPassword);
    }

    public void checkSearchData(String destination) throws InvalidFieldException {
        checkOnlyLetters(destination);
    }

    public void checkBuyData(String clientName, List<String> touristsList) throws InvalidFieldException {
        checkOnlyLetters(clientName);
        for(String name: touristsList)
            checkName(name);
    }
}
