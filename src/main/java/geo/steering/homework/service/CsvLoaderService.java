package geo.steering.homework.service;

import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;

public interface CsvLoaderService {
  
  void loadCsvFiles() throws IOException, CsvValidationException;
  
  
}
