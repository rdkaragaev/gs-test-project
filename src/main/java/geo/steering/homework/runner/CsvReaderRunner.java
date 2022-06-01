package geo.steering.homework.runner;

import geo.steering.homework.service.CsvLoaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CsvReaderRunner implements CommandLineRunner {
  
  private final CsvLoaderService csvLoaderService;
  
  @Override
  public void run(String... args) throws Exception {
    csvLoaderService.loadCsvFiles();
  }
  
}

