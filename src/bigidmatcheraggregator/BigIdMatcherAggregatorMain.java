package bigidmatcheraggregator;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import bigidmatcheraggregator.interfaces.FileReaderService;
import bigidmatcheraggregator.interfaces.NameMatcherService;
import bigidmatcheraggregator.interfaces.ResultAggregatorService;
import bigidmatcheraggregator.interfaces.UserInputService;
import bigidmatcheraggregator.service.FileReaderServiceImpl;
import bigidmatcheraggregator.service.NameMatcherServiceImpl;
import bigidmatcheraggregator.service.ResultAggregatorServiceImpl;
import bigidmatcheraggregator.service.UserInputServiceImpl;

public class BigIdMatcherAggregatorMain {

	// URL of the text file containing data to be processed
	private static final String FILE_URL = "http://norvig.com/big.txt";
	
	// Array of common names to search for in the text
    private static final List<String> COMMON_NAMES = Arrays.asList(
        "James", "John", "Robert", "Michael", "William",
        "David", "Richard", "Charles", "Joseph", "Thomas",
        "Christopher", "Daniel", "Paul", "Mark", "Donald",
        "George", "Kenneth", "Steven", "Edward", "Brian",
        "Ronald", "Anthony", "Kevin", "Jason", "Matthew",
        "Gary", "Timothy", "Jose", "Larry", "Jeffrey",
        "Frank", "Scott", "Eric", "Stephen", "Andrew",
        "Raymond", "Gregory", "Joshua", "Jerry", "Dennis",
        "Walter", "Patrick", "Peter", "Harold", "Douglas",
        "Henry", "Carl", "Arthur", "Ryan", "Roger"
    );

    public static void main(String[] args) {

        // Create a scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Initialize services for reading files, matching names, and aggregating results
        FileReaderService fileReaderService = new FileReaderServiceImpl();
        NameMatcherService nameMatcherService = new NameMatcherServiceImpl(COMMON_NAMES);
        ResultAggregatorService resultAggregatorService = new ResultAggregatorServiceImpl();

        try {
            // Read the content of the file from the specified URL
            fileReaderService.readFileFromUrl(FILE_URL, nameMatcherService, resultAggregatorService, COMMON_NAMES);

            // Create a user input service to handle user interactions with the aggregated results
            UserInputService userInputService = new UserInputServiceImpl(scanner, resultAggregatorService.getCombinedResults());

            // Start handling user input
            userInputService.handleUserInput();
        } catch (Exception e) {
            // Print stack trace for any exceptions that occur
            e.printStackTrace();
        } finally {
            // Ensure the scanner is closed to free up resources
            scanner.close();
        }
    }
}