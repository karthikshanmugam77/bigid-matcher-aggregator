package bigidmatcheraggregator.interfaces;

public interface NameMatcherService {
    /**
     * Finds all occurrences of a specified name within a given line.
     * 
     * @param line The text line to search through.
     * @param name The name to be searched for in the line.
     * @param linePosition The line number offset for the occurrences.
     * @param resultAggregatorService The service used to aggregate results of name matches.
     */
    void findNames(String line, String name, long linePosition, ResultAggregatorService resultAggregatorService);
}