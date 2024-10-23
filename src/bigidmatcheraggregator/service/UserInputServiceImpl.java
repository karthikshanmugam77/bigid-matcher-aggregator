package bigidmatcheraggregator.service;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import bigidmatcheraggregator.exception.CustomException;
import bigidmatcheraggregator.interfaces.UserInputService;

public class UserInputServiceImpl implements UserInputService {
	private final Scanner scanner;
    private final Map<String, List<String>> combinedResults;

    public UserInputServiceImpl(Scanner scanner, Map<String, List<String>> combinedResults) {
        this.scanner = scanner;
        this.combinedResults = combinedResults;
    }

    @Override
    public void handleUserInput() throws CustomException {
        System.out.println("--------- Choose an option -------------");
        System.out.println("--------- 1. Show all names ------------");
        System.out.println("--------- 2. Show specific name --------");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer

        switch (choice) {
            case 1:
                showAllNames();
                break;
            case 2:
                showSpecificName();
                break;
            default:
                throw new CustomException("Invalid Input value");
        }
    }

    private void showAllNames() {
        for (String name : combinedResults.keySet()) {
            List<String> offsets = combinedResults.get(name);
            if (offsets != null && !offsets.isEmpty()) {
                System.out.println(name + " ------> " + offsets);
            }
        }
    }

    private void showSpecificName() {
        System.out.println("--------- Enter name to search --------");
        String inputValue = scanner.nextLine();
        List<String> offsets = combinedResults.get(inputValue.toLowerCase());
        if (offsets != null && !offsets.isEmpty()) {
            System.out.println(inputValue + " ------> " + offsets);
        } else {
            System.out.println("No occurrences found for: " + inputValue);
        }
    }
}