package edu.mephi.reactor;

import edu.mephi.exceptions.WrongFileFormatException;
import edu.mephi.reactor.readers.Reader;
import edu.mephi.reactor.readers.ReaderJSON;
import edu.mephi.reactor.readers.ReaderXML;
import edu.mephi.reactor.readers.ReaderYAML;
import java.io.IOException;

public class ReactorStorageMaker {
  private Reader firstReader;

  public ReactorStorageMaker() {
    this.firstReader = new ReaderXML();
    Reader secondReader = new ReaderYAML();
    Reader thirdReader = new ReaderJSON();
    firstReader.setNext(secondReader);
    secondReader.setNext(thirdReader);
  }

  public ReactorStorage makeReactorStorage(String filename)
      throws WrongFileFormatException, IOException {
    return firstReader.handleFile(filename);
  }
}
