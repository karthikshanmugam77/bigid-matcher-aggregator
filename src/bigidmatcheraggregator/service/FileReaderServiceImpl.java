package bigidmatcheraggregator.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bigidmatcheraggregator.interfaces.FileReaderService;
import bigidmatcheraggregator.interfaces.NameMatcherService;
import bigidmatcheraggregator.interfaces.ResultAggregatorService;

public class FileReaderServiceImpl implements FileReaderService {

    private static final int BATCH_SIZE = 1000;

    @Override
    public void readFileFromUrl(String url, NameMatcherService nameMatcherService, ResultAggregatorService resultAggregatorService, List<String> commonNames) throws Exception {
        URL fileUrl = new URL(url);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileUrl.openStream()))) {
            String line;
            List<String> batch = new ArrayList<>();
            long lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                batch.add(line);

                // When we reach 1000 lines, process the batch
                if (batch.size() == BATCH_SIZE) {
                    final List<String> linesToProcess = new ArrayList<>(batch); // Capture the current batch
                    final long currentLineNumber = lineNumber - batch.size() + 1; 
                    executor.submit(() -> processBatch(linesToProcess, nameMatcherService, resultAggregatorService, commonNames, currentLineNumber));
                    batch.clear(); // Clear the batch for new lines
                }
            }

            // Process any remaining lines in the last batch
            if (!batch.isEmpty()) {
                final long currentLineNumber = lineNumber - batch.size() + 1; // Create a final variable
                executor.submit(() -> processBatch(batch, nameMatcherService, resultAggregatorService, commonNames, currentLineNumber));
            }
        } finally {
            executor.shutdown();
            while (!executor.isTerminated()) {
                // Wait for all tasks to finish
            }
        }
    }

    private void processBatch(List<String> lines, NameMatcherService nameMatcherService, ResultAggregatorService resultAggregatorService, List<String> commonNames, long startingLineNumber) {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (String name : commonNames) {
                nameMatcherService.findNames(line, name, startingLineNumber + i, resultAggregatorService);
            }
        }
    }
}