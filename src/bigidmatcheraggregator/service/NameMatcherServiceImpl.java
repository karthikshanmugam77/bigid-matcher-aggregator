package bigidmatcheraggregator.service;

import java.util.ArrayList;
import java.util.List;

import bigidmatcheraggregator.interfaces.NameMatcherService;

public class NameMatcherServiceImpl implements NameMatcherService {

	/**
	 * Finds all occurrences of a given name within the provided content.
	 * 
	 * @param content      The text content to search through.
	 * @param name         The name to find in the content.
	 * @param linePosition The starting line position for offsets.
	 * @return A list of offset information for each occurrence of the name.
	 */
	@Override
	public List<String> findNames(String content, String name, long linePosition) {
		List<String> offsets = new ArrayList<>();
		String lowerCaseName = name.toLowerCase();

		// Split the content into individual lines for line-based searching
		String[] lines = content.split("\n");

		// Iterate through each line in the content
		for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
			String lowerCaseLine = lines[lineNumber].toLowerCase();
			int index = lowerCaseLine.indexOf(lowerCaseName);
			while (index != -1) {
				// Create offset information including line and character position
				String offsetInfo = "{lineOffset=" + (linePosition + lineNumber + 1) + ", charOffset=" + (index + 1) + "}";
				offsets.add(offsetInfo);
				// Search for the next occurrence of the name in the line
				index = lowerCaseLine.indexOf(lowerCaseName, index + 1);
			}
		}
		// Return the list of offsets for all occurrences found
		return offsets;
	}
}
