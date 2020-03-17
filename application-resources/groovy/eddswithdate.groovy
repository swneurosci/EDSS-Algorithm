new File(scriptParams.outputFile).withWriterAppend{ out ->
  doc.getAnnotations("MS").get("EDSS").each{
    anno ->
      def f = anno.getFeatures()
      def (A,B,C) =  doc.getFeatures().get("gate.SourceURL").split("_|\\.")
      String[] id = A.split("/")
      out.writeLine(/"${id[-1]}","${f.get('Date')}","${f.get('value')}","${f.get('when')}"/)
  }
}