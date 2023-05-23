package edu.mephi.reactor;

// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.dataformat.xml.XmlMapper;
// import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
// import edu.mephi.exceptions.WrongFileFormatException;
// import java.io.File;
// import java.io.IOException;
import java.util.Map;

public class ReactorStorage {
  private Map<String, Reactor> storage;
  private String type;

  public Map<String, Reactor> getStorage() { return storage; }
  public void setStorage(Map<String, Reactor> storage) {
    this.storage = storage;
  }
  public String getType() { return type; }
  public void setType(String type) { this.type = type; }
}
