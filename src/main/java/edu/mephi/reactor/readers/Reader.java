package edu.mephi.reactor.readers;

import java.io.IOException;

import edu.mephi.exceptions.WrongFileFormatException;
import edu.mephi.reactor.ReactorStorage;

public abstract class Reader {
  protected Reader next;

  public Reader getNext() {
    return next;
  }

  public void setNext(Reader next) {
    this.next = next;
  }

  public abstract ReactorStorage handleFile(String filename)
  throws WrongFileFormatException, IOException;

}
