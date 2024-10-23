package bigidmatcheraggregator.interfaces;

import java.util.List;
import java.util.Map;

public interface ResultAggregatorService {
    
    /**
     * Aggregates the results for a specified name.
     * 
     * @param name The name for which results are being aggregated.
     * @param results A list of strings representing the positions of occurrences of the name.
     */
    void aggregateResults(String name, List<String> results);
    
    /**
     * Retrieves the combined results of all aggregated names.
     * 
     * @return A map where the keys are names and the values are lists of strings representing 
     *         the positions of each occurrence for those names.
     */
    Map<String, List<String>> getCombinedResults();
}