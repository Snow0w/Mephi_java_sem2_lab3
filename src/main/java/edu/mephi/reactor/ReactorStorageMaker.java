package edu.mephi.reactor;

import java.io.IOException;

import edu.mephi.exceptions.WrongFileFormatException;
import edu.mephi.reactor.readers.Reader;
import edu.mephi.reactor.readers.ReaderJSON;
import edu.mephi.reactor.readers.ReaderXML;
import edu.mephi.reactor.readers.ReaderYAML;

public class ReactorStorageMaker {
  private Reader firstReader;
  private Reader secondReader;
  private Reader thirdReader;

  public ReactorStorageMaker() {
    this.firstReader = new ReaderXML();
    this.secondReader = new ReaderYAML();
    this.thirdReader = new ReaderJSON();
    firstReader.setNext(secondReader);
    secondReader.setNext(thirdReader);
  }

  public ReactorStorage makeReactorStorage(String filename) 
      throws WrongFileFormatException, IOException {
    return firstReader.handleFile(filename);
  }
  
}
