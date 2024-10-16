package bigidmatcheraggregator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import bigidmatcheraggregator.exception.CustomException;

public class BigIdMatcherAggregatorMain {
	// Buffer size for processing data
	private static final int BUFFER_SIZE = 1000;

	// URL of the text file
	private static final String FILE_URL = "http://norvig.com/big.txt";

	// Map to hold combined results, with each key as a name and value as a list of
	// occurrence offsets.
	static Map<String, List<String>> combinedResults = new HashMap<>();
	static List<Future<Map<String, List<String>>>> futures = new ArrayList<>();

	// Current line position being processed
	private static long currentLinePosition = 1L;
	// Current character position in the line being processed
	private static long currentCharPosition = 1L;

	// Array of common names to search for in the content.
	static String[] commonNamesArray = { "James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles",
			"Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth", "Steven",
			"Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry",
			"Jeffrey", "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis",
			"Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger" };

	// Scanner for user input
	static Scanner scanner = new Scanner(System.in);

	// StringBuilder for accumulating content
	static StringBuilder contentBuilder = new StringBuilder();

	static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public static void main(String[] args) {
		try {
			readFileFromUrl();
			aggregateResults();
			handleUserInput();
		} catch (Exception e) {
			e.getLocalizedMessage();
		} finally {
			executorService.shutdown(); // Shutdown executor service
			scanner.close(); // Close scanner
		}
	}

	private static void readFileFromUrl() throws Exception {
		URL url = new URL(FILE_URL);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				contentBuilder.append(line).append("\n");
				processInBatch(contentBuilder.toString());
				++currentLinePosition;
			}
			if (contentBuilder.length() > 0) {
				findAndUpdateCommonNames(contentBuilder.toString(), commonNamesArray, currentLinePosition);
			}
		}
	}

	private static void processInBatch(String content) throws InterruptedException, ExecutionException {
		if (currentLinePosition > 0 && currentLinePosition % BUFFER_SIZE == 0) {
			findAndUpdateCommonNames(content, commonNamesArray, currentLinePosition - BUFFER_SIZE);
			contentBuilder.setLength(0); // Clear the accumulated content
		}
	}

	public static void findAndUpdateCommonNames(String content, String[] names, long linePosition)
			throws InterruptedException, ExecutionException {
		for (String name : names) {
			Callable<Map<String, List<String>>> task = new NameMatcherTask(content, name, linePosition);
			futures.add(executorService.submit(task)); // Submit task to the executor
		}
	}

	static class NameMatcherTask implements Callable<Map<String, List<String>>> {
		private final String content; // Content to search in
		private final String name; // Name to search for
		private long linePosition; // Current line position

		public NameMatcherTask(String content, String name, long linePosition) {
			this.content = content;
			this.name = name;
			this.linePosition = linePosition;
		}

		@Override
		public Map<String, List<String>> call() {
			Map<String, List<String>> nameOffsetsMap = new HashMap<>();
			String lowerCaseName = name.toLowerCase();
			nameOffsetsMap.put(lowerCaseName, new ArrayList<>());

			if (content != null) {
				String[] lines = content.split("\n");
				for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
					String lowerCaseLine = lines[lineNumber].toLowerCase();
					int index = lowerCaseLine.indexOf(lowerCaseName);
					while (index != -1) {
						String offsetInfo = "{lineOffset=" + (linePosition + lineNumber + 1) + ", charOffset="
								+ (currentCharPosition + index) + "}";
						nameOffsetsMap.get(lowerCaseName).add(offsetInfo);
						index = lowerCaseLine.indexOf(lowerCaseName, index + 1);
					}
					currentCharPosition += lines[lineNumber].length() + 1;
				}
			}
			return nameOffsetsMap;
		}
	}

	static void aggregateResults() throws InterruptedException, ExecutionException {
		for (Future<Map<String, List<String>>> task : futures) {
			Map<String, List<String>> result = task.get();
			if (result != null) {
				result.forEach((key, value) -> {
					combinedResults.putIfAbsent(key.toLowerCase(), new ArrayList<>());
					combinedResults.get(key.toLowerCase()).addAll(value);
				});
			}
		}
	}

	static void handleUserInput() throws CustomException {
		System.out.println("--------- Choose an option -------------");
		System.out.println("--------- 1. Show all names ------------");
		System.out.println("--------- 2. Show specific name --------");
		int choice = scanner.nextInt();

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

	static void showSpecificName() {
		scanner.nextLine(); // Clear the buffer
		System.out.println("--------- Enter name to search --------");
		String inputValue = scanner.nextLine();
		List<String> offsets = combinedResults.get(inputValue.toLowerCase());
		if (offsets != null && !offsets.isEmpty()) {
			System.out.println(inputValue + " ------> " + offsets);
		} else {
			System.out.println("No occurrences found for: " + inputValue);
		}
	}

	static void showAllNames() {
		for (String name : commonNamesArray) {
			List<String> offsets = combinedResults.get(name.toLowerCase());
			if (offsets != null && !offsets.isEmpty()) {
				System.out.println(name + " ------> " + offsets);
			}
		}
	}
}
