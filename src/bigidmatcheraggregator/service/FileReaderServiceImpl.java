package bigidmatcheraggregator.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import bigidmatcheraggregator.interfaces.FileReaderService;

public class FileReaderServiceImpl implements FileReaderService {

	@Override
	public String readFileFromUrl(String url) throws Exception {
		StringBuilder contentBuilder = new StringBuilder();
		URL fileUrl = new URL(url);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileUrl.openStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				contentBuilder.append(line).append("\n");
			}
		}
		return contentBuilder.toString();
	}

}
