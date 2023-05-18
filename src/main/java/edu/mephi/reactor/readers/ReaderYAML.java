package edu.mephi.reactor.readers;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import edu.mephi.exceptions.WrongFileFormatException;
import edu.mephi.reactor.Reactor;
import edu.mephi.reactor.ReactorStorage;

public class ReaderYAML extends Reader{

	@Override
	public ReactorStorage handleFile(String filename)
      throws WrongFileFormatException, IOException {
    if (!filename.endsWith(".yaml")) {
      if (this.next == null) {
        throw new WrongFileFormatException("Wrong file format");
      }
      return this.next.handleFile(filename);
    }
    ReactorStorage reactors = new ReactorStorage();
    reactors.setType("yaml");
    Map<String, Reactor> storage;
    ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    storage = objectMapper.readValue(
        new File(filename), new TypeReference<Map<String, Reactor>>() {});
    reactors.setStorage(storage);
    return reactors;
	}
  
}

