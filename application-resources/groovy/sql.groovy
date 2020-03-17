import groovy.sql.Sql


//def sql = Sql.newInstance("jdbc:mysql://localhost:3306/test?useLegacyDatetimeCode=false&serverTimezone=America/New_York", "root","tl2zoaf4", "com.mysql.jdbc.Driver")

def result = []
def createResult = {
        result += it
}
String inputDocName = doc.getName();
String lastDocName = corpus.get(corpus.size()-1).getName();

 //if (inputDocName.equals(lastDocName)){
  doc.getAnnotations("MS").get("EDSS").each{ anno ->
      def f = anno.getFeatures()
      def (A,B,C) =  doc.getFeatures().get("gate.SourceURL").split("_|\\.")
      String appt = B.replace('%20', '-')
      String[] id = A.split("/")
      def dat = [[id[-1], appt, f.get('value'), f.get('nhs_value')]]
      createResult(dat)
  }
 //}

  def sql = Sql.newInstance("jdbc:mysql://localhost:3306/test?useLegacyDatetimeCode=false&serverTimezone=America/New_York", "root","tl2zoaf4", "com.mysql.jdbc.Driver")
 
  sql.execute '''DROP TABLE IF EXISTS EDSS'''
  sql.execute '''DROP TABLE IF EXISTS MS_EDSS'''

        sql.execute '''
        CREATE TABLE EDSS (
        id          INT,
        Clinic   VARCHAR(15),
        EDSSscore    VARCHAR(64),
        NHS     VARCHAR(64)
        );
        '''

    sql.withBatch("INSERT INTO EDSS (id, Clinic, EDSSscore, NHS) VALUES (?,?,?,?)"){ bt ->                      
      result.each { row ->
      bt.addBatch (row)
      }
  }
  
  sql.execute '''
  CREATE TABLE MS_EDSS AS
  SELECT * FROM EDSS WHERE EDSSScore is not null;
      ''' 
  def res = sql.rows("select distinct id, min(EDSSscore) from MS_EDSS group by id")
  //sql.eachRow("select distinct id, min(EDSSscore) from MS_EDSS group by id"){ row ->
    //    new File(scriptParams.outputFile).withWriterAppend{ out ->
      //  out.writeLine(/"${row}"/)
        //}
  //}

  new File(scriptParams.outputFile).withWriterAppend{ out ->
      out.writeLine(/"${res}"/)
  }

