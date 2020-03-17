List<Annotation> sentList = new ArrayList<Annotation>(inputAS.get("MoreMatch").inDocumentOrder());
String outPutFileName = scriptParams.outputFile;
String docContent = doc.getContent().toString();
FeatureMap docFeature = doc.getFeatures();
String docName = doc.getName();
if (sentList.size()>0){
for (int i=0 ; i < sentList.size(); i++){
Annotation sentAnno = sentList.get(i);
Long startoffset = sentAnno.getStartNode().getOffset();
Long endoffset = sentAnno.getEndNode().getOffset();
String more_sentence = docContent.substring((int)startoffset, (int)endoffset);
more_sentence = more_sentence.replace('\r\n',' ')
more_sentence = more_sentence.replace('\n',' ')
more_sentence = more_sentence.replace('\t',' ')
FeatureMap sentFeatures = sentAnno.getFeatures();
long sentence_start = sentFeatures.get("sent_start_span")
long sentence_end = sentFeatures.get("sent_end_span")
String sentence = docContent.substring((int)sentence_start, (int)sentence_end);
sentence = sentence.replace('\r\n',' ')
sentence = sentence.replace('\n',' ')
sentence = sentence.replace('\t',' ')
String terms = sentFeatures.get("terms");
terms = terms.replace('\r\n',' ')
terms = terms.replace('\n',' ')
terms = terms.replace('\t',' ')
singleoutput = docName+"\t"+terms+"\t"+sentence+"\t"+more_sentence;
//singleoutput = sentence
optFile << singleoutput+'\r\n'
println(singleoutput)
}
}
