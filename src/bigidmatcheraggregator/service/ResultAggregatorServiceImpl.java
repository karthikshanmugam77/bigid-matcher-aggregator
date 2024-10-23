package bigidmatcheraggregator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bigidmatcheraggregator.interfaces.ResultAggregatorService;

public class ResultAggregatorServiceImpl implements ResultAggregatorService {
    private Map<String, List<String>> combinedResults = new HashMap<>();

    @Override
    public void aggregateResults(String name, List<String> offsets) {
        combinedResults.putIfAbsent(name.toLowerCase(), new ArrayList<>());
        combinedResults.get(name.toLowerCase()).addAll(offsets);
    }

    public Map<String, List<String>> getCombinedResults() {
        return combinedResults;
    }
}
