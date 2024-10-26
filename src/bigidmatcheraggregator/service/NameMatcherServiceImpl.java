package bigidmatcheraggregator.service;

import java.util.List;

import bigidmatcheraggregator.interfaces.NameMatcherService;
import bigidmatcheraggregator.interfaces.ResultAggregatorService;

public class NameMatcherServiceImpl implements NameMatcherService {
	private List<String> commonNames;

	// Constructor accepting a list of common names
	public NameMatcherServiceImpl(List<String> commonNames) {
		this.commonNames = commonNames;
	}

	public void findAllNames(String line, long linePosition, ResultAggregatorService resultAggregatorService) {
		for (String name : commonNames) {
			findNames(line, name, linePosition, resultAggregatorService);
		}
	}

	@Override
	public void findNames(String line, String nameToSearch, long linePosition,
			ResultAggregatorService resultAggregatorService) {
		String lowerCaseName = nameToSearch.toLowerCase();
		String lowerCaseLine = line.toLowerCase();
		int index = lowerCaseLine.indexOf(lowerCaseName);

		while (index != -1) {
			// Create offset information including line and character position
			String offsetInfo = "{lineOffset=" + linePosition + ", charOffset=" + (index + 1) + "}";
			resultAggregatorService.aggregateResults(nameToSearch, List.of(offsetInfo));
			// Search for the next occurrence of the name in the line
			index = lowerCaseLine.indexOf(lowerCaseName, index + 1);
		}
	}
}