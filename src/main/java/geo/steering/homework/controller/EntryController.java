package geo.steering.homework.controller;

import geo.steering.homework.service.EntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class EntryController {
  
  @Value("${default.page.size:20}")
  private Integer defaultPageSize;
  
  private final EntryService entryService;

  @GetMapping("/entry")
  public String getResult(Model model,
                          Pageable pageable,
                          @RequestParam(value = "q") String jsonKey,
                          @RequestParam(required = false, defaultValue = "false") Boolean distinct)
  {
    var entries = entryService.getByJsonKey(jsonKey, distinct, pageable);
    
    model.addAttribute("jsonKey", jsonKey);
    model.addAttribute("entries", entries);
    
    return "entry";
  }
  
}
