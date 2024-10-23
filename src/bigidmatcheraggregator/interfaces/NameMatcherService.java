package bigidmatcheraggregator.interfaces;

import java.util.List;

public interface NameMatcherService {
	/**
     * Finds all occurrences of a specified name within the given content.
     * 
     * @param content The text content to search through.
     * @param name The name to be searched for in the content.
     * @param linePosition The line number offset for the occurrences.
     * @return A list of strings representing the positions of each occurrence, 
     *         formatted as "{lineOffset=X, charOffset=Y}".
     */
	List<String> findNames(String content, String name, long linePosition);
}
