package geo.steering.homework.service.impl;

import geo.steering.homework.mapper.EntryMapper;
import geo.steering.homework.model.Entry;
import geo.steering.homework.service.EntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {
  
  @Value("${batch.size:10000}")
  private Integer batchSize;
  
  private final SqlSessionFactory sessionFactory;
  
  @Override
  public List<Entry> getByJsonKey(String jsonKey, Boolean distinct, Pageable pageable) {
    try (SqlSession session = sessionFactory.openSession()) {
      EntryMapper mapper = session.getMapper(EntryMapper.class);
      
      if (!mapper.isJsonKeyExist(jsonKey)) {
        return Collections.emptyList();
      }
      
      if (distinct) {
        return mapper.getByDistinctJsonKey(jsonKey, getRowBounds(pageable));
      }
      
      return mapper.getByJsonKey(jsonKey, getRowBounds(pageable));
    }
  }
  
  private RowBounds getRowBounds(Pageable pageable) {
    return new RowBounds((int) pageable.getOffset(), pageable.getPageSize());
  }
  
  @Override
  public void saveAll(List<Entry> entries) {
    try (SqlSession session = sessionFactory.openSession(ExecutorType.BATCH, false)) {
      EntryMapper mapper = session.getMapper(EntryMapper.class);
    
      int i = 0;
      int size = entries.size() - 1;
    
      for (Entry entry : entries) {
        mapper.insert(entry);
        i++;
      
        if (i % this.batchSize == 0 || i == size) {
          session.flushStatements();
          session.clearCache();
        }
      }
    
      session.commit();
    }
  }
  
  public boolean isEmpty() {
    try (SqlSession session = sessionFactory.openSession()) {
      EntryMapper mapper = session.getMapper(EntryMapper.class);

      Integer estimateCount = mapper.estimateCount();
      
      if (estimateCount == null || estimateCount == 0) {
        return true;
      }
    }
    
    return false;
  }
  
}
