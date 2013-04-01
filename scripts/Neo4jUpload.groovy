import com.gsgenetics.genie.VcfFile
import com.gsgenetics.framework.parser.VcfParser







VcfParser parser = new VcfParser(new StringReader(""));
def records = []
EXTENSION = ~/.*final\.vcf$/
def files = []


def run = "/Users/afrieden/test/VCFData/"
new File(run).eachFileMatch(EXTENSION) { files << it.name }

println "getting vcf records"

files.each {
    name ->

        new File(run + name).eachLine
                {
                    if(!it.startsWith("#"))
                    {
                        records += VcfParser.parseOneLine(it)
                    }
                }
}
println "the number of records is " + records.size
ArrayList<VcfFile> MyFiles = new ArrayList<VcfFile>()
files.each {
    VcfFile MyFile = new VcfFile(run + it)
    MyFiles.add(MyFile)
}

//tab delimited column names

//Q1 is depth
//Q2 is strand bias
//Q3 is quality/depth
//Q4 is Allele Ratio

//Create Connection To Neo4j




/**
* Get contig metadata, parse reformatted strings and return Locus objects
* representing full-length chromosomes
*/
def RunID = ""
def sampleID = ""

def date = new Date()
def formattedDate = date.format('yyyyMMdd')






println("records=" + records.size())
println("files=" + MyFiles.size())

println "run\tsample\tchromosome\tposition\treference\talt\tannotation\tcalled\tQ1\tQ2\tQ3\tQ4"
MyFiles.each
        {
            file -> file
                def MyRegExData = file.getSampleIdentifiers()
                def VCFdatapieces = MyRegExData[0].split('/')
                sampleID = "NOSAMPLEID"
                for(int j =0;j<VCFdatapieces.size();j++)
                {
                    if(VCFdatapieces[j].toString().startsWith("PAT"))
                    {
                        def BadSampleId = VCFdatapieces[j].toString().split('-')

                        sampleID = BadSampleId[0]
                    }
                }
                RunID = "NORUNID"
                for(int j=0;j<VCFdatapieces.size();j++)
                {
                    if(VCFdatapieces[j].toString().startsWith("HIP"))
                    {
                        RunID = VCFdatapieces[j].toString()
                    }
                }

                file.each
                        {
                            record -> record
                                def MyAnnotation = "UNKNOWN"

                                try {
                                    MyAnnotation = record.getGsgDatabaseName()
                                } catch (e) {
                                    // name will be UNKNOWN
                                }
                                //<editor-fold desc="Quality Scores and Callable">
                                def uncallable = record.isUncallable()
                                def qs1 = "NO QS1"
                                def qs2 = "NO QS2"
                                def qs3 = "NO QS3"
                                def qs4 = "NO QS4"
                                try {
                                    qs1 = record.getQs1()
                                } catch(e){

                                }
                                try {
                                    qs2 = record.getQs2()
                                } catch(e){

                                }
                                try {
                                    qs3 = record.getQs3()
                                } catch(e){

                                }
                                try {
                                    qs4 = record.getQs4()
                                } catch(e){

                                }
                                //</editor-fold>

                                println("${RunID}\t${sampleID}\t${record.getContig()}\t${record.getPosition()}\t${record.reference}\t${record.getAlternateAlleles()}\t${MyAnnotation}\t${uncallable}\t${qs1}\t${qs2}\t${qs3}\t${qs4}")
                                /*
                                Now add a node
                                */


                        }
        }
