package bigidmatcheraggregator.interfaces;

import bigidmatcheraggregator.exception.CustomException;

public interface UserInputService {
    
    /**
     * Handles user input for interacting with the application.
     * 
     * This method may prompt the user for input and process the input 
     * according to the application's requirements. It can throw a 
     * CustomException to signal issues with the input or processing.
     * 
     * @throws CustomException If there are issues processing user input.
     */
    void handleUserInput() throws CustomException;
}