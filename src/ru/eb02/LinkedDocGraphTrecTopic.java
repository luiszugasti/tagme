package ru.eb02;

public class LinkedDocGraphTrecTopic {
  DocGraph docGraph;
  trecTopic tt;

  public LinkedDocGraphTrecTopic(DocGraph docGraph, trecTopic tt) {
    this.docGraph = docGraph;
    this.tt = tt;
  }

  public DocGraph getDocGraph() {
    return docGraph;
  }

  public trecTopic getTt() {
    return tt;
  }


}
