package geo.steering.homework.mapper;

import geo.steering.homework.model.Entry;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface EntryMapper {

  @Insert("INSERT INTO entry (filename, line, data) VALUES (#{filename}, #{line}, #{data}::jsonb)")
  void insert(Entry entry);
  
  @Select("SELECT filename, line, data->>'${jk}' AS data FROM entry WHERE data->>'${jk}' IS NOT NULL ORDER BY filename, data->>'${jk}'")
  List<Entry> getByJsonKey(@Param("jk") String jsonKey, RowBounds rowBounds);
  
  @Select("SELECT DISTINCT ON (filename, data->>'${jk}') filename, line, data->>'${jk}' AS data FROM (SELECT * FROM entry WHERE data->>'${jk}' IS NOT NULL) t ORDER BY filename, data->>'${jk}'")
  List<Entry> getByDistinctJsonKey(@Param("jk") String jsonKey, RowBounds rowBounds);
  
  @Select("SELECT EXISTS (SELECT FROM entry WHERE (data->'${jk}') IS NOT NULL)")
  boolean isJsonKeyExist(@Param("jk") String jsonKey);
  
  @Select("SELECT reltuples AS estimate FROM pg_class where relname = 'entry'")
  Integer estimateCount();

}
