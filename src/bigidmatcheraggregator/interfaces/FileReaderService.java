package bigidmatcheraggregator.interfaces;

import java.util.List;

public interface FileReaderService {
    /**
     * Reads the content of a file from the specified URL and processes it in batches.
     * 
     * @param url The URL of the file to be read.
     * @param nameMatcherService The service used to match names in the content.
     * @param resultAggregatorService The service used to aggregate results of name matches.
     * @throws Exception If an error occurs while reading the file, such as a network issue or invalid URL.
     */
    void readFileFromUrl(String url, NameMatcherService nameMatcherService, ResultAggregatorService resultAggregatorService, List<String> commonNames) throws Exception;
}