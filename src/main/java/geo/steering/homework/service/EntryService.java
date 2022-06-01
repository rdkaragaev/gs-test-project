package geo.steering.homework.service;

import geo.steering.homework.model.Entry;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EntryService {
  
  List<Entry> getByJsonKey(String jsonKey, Boolean distinct, Pageable pageable);
  
  void saveAll(List<Entry> entries);
  
  boolean isEmpty();
  
}
