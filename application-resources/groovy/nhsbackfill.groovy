import groovy.sql.Sql

def sql = Sql.newInstance("jdbc:mysql://localhost:3306/test?useLegacyDatetimeCode=false&serverTimezone=America/New_York", "root","tl2zoaf4", "com.mysql.jdbc.Driver")

sql.execute '''DROP TABLE IF EXISTS EDSS'''

        sql.execute '''
        CREATE TABLE EDSS (
        id          INT,
        Clinic   VARCHAR(15),
        EDSSscore    VARCHAR(64),
        NHS     VARCHAR(64)
        );

        '''
def result = []
def createResult = {
        result += it
}
new File(scriptParams.outputFile).withWriterAppend{ out ->
      doc.getAnnotations("MS").get().each{
        anno ->
          def f = anno.getFeatures()
          def (A,B,C) =  doc.getFeatures().get("gate.SourceURL").split("_|\\.")
          String appt = B.replace('%20', '-')
          String[] id = A.split("/")
          def dat = [[id[-1], appt, f.get('value'), f.get('nhs_value')]]
          createResult(dat)       
      }     


        sql.withBatch("INSERT INTO EDSS (id, Clinic, EDSSscore, NHS) VALUES (?,?,?,?)"){ bt ->                      
            result.each { row ->
                bt.addBatch (row) 
            }
        }

        sql.execute '''DROP TABLE IF EXISTS MS_EDSS'''
        sql.execute '''
        CREATE TABLE MS_EDSS AS
        SELECT * FROM EDSS WHERE EDSSScore is not null;
        ''' 
        
        res = sql.rows("select distinct id, min(EDSSscore) as minScore from EDSS group by id")
        
        
        out.writeLine(/"${res}"/)
        }
