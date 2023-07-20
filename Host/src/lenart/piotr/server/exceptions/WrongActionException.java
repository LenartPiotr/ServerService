package lenart.piotr.server.exceptions;

public class WrongActionException extends Exception{
    private String textToDisplay;
    public WrongActionException(String text){
        textToDisplay = text;
    }
    public String getTextToDisplay() {
        return textToDisplay;
    }
}
