package bigidmatcheraggregator.interfaces;

public interface FileReaderService {
	/**
     * Reads the content of a file from the specified URL.
     * 
     * @param url The URL of the file to be read.
     * @return A string containing the content of the file.
     * @throws Exception If an error occurs while reading the file, such as a network issue or invalid URL.
     */
	String readFileFromUrl(String url) throws Exception;

}
