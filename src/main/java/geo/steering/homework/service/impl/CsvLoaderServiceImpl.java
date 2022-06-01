package geo.steering.homework.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import geo.steering.homework.model.Entry;
import geo.steering.homework.service.CsvLoaderService;
import geo.steering.homework.service.EntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvLoaderServiceImpl implements CsvLoaderService {
  
  @Value("classpath:csv/*.csv")
  Resource[] csvResources;
  
  private final ObjectMapper objectMapper;
  private final EntryService entryService;
  
  @Override
  public void loadCsvFiles() throws IOException, CsvValidationException {
    if (!entryService.isEmpty()) {
      return;
    }
    
    log.info("[loadCsvFiles] START");
    Instant loadAllStart = Instant.now();
    
    for (Resource csvResource : csvResources) {
      String filename = csvResource.getFilename();
      log.info("Now loading: {}, size {} bytes", filename, csvResource.getFile().length());
  
      Instant loadStart = Instant.now();
      
      CSVReaderHeaderAware csvReader =
              new CSVReaderHeaderAware(new FileReader(csvResource.getFile()));

      Map<String, String> record;
      List<Entry> entries = new ArrayList<>();

      while ((record = csvReader.readMap()) != null) {
        Entry entry = new Entry();
        entry.setFilename(csvResource.getFilename());
        entry.setLine(csvReader.getLinesRead());
        entry.setData(getJsonString(record));

        entries.add(entry);
      }

      entryService.saveAll(entries);
  
      Instant loadEnd = Instant.now();
      Duration loadDuration = Duration.between(loadStart, loadEnd);
      log.info(" - Loaded and persisted in {}ms ({}sec)", loadDuration.toMillis(), loadDuration.toSeconds());
    }
    
    Instant loadAllEnd = Instant.now();
    Duration loadAllDuration = Duration.between(loadAllStart, loadAllEnd);
    log.info("[loadCsvFiles] END. Time taken: {}ms ({}sec)", loadAllDuration.toMillis(), loadAllDuration.toSeconds());
  }
  
  private String getJsonString(Map<String, String> record) {
    try {
      return objectMapper.writeValueAsString(record);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
