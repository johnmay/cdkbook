// Copyright (c) 2018  Egon Willighagen <egon.willighagen@gmail.com>
//
// GPL v3

input = args[0]

bibliography = new HashMap<String,String>();
def bibLines = new File("references.dat").readLines()
bibLines.each { String line ->
  fields = line.split("=")
  bibliography.put(fields[0], fields[1])
}

references = new HashMap<String,String>();
bibList = "";
refCounter = 0;

def lines = new File(input).readLines()
lines.each { String line ->
  if (line.contains("<code>")) {
    def instruction = new XmlSlurper().parseText(line)
    def srcLines = new File("code/${instruction.text()}.verbatim.md").readLines()
    srcLines.each { String srcLine -> println srcLine }
  } else if (line.contains("<out>")) {
    def instruction = new XmlSlurper().parseText(line)
    println "```plain"
    def srcLines = new File("code/${instruction.text()}.out").readLines()
    srcLines.each { String srcLine -> println srcLine }
    println "```"
  } else if (line.contains("<references/>")) {
    println bibList
  } else {
    while (line.contains(".i.md")) {
      line = line.replace(".i.md", ".md")
    }
    while (line.contains("<cite>")) {
      citeStart = line.indexOf("<cite>")
      citeEnd = line.indexOf("</cite>")
      cites = line.substring(citeStart+6, citeEnd)
      replacement = ""
      if (!references.containsKey(cites)) {
        refCounter++
        references.put(cites, "" + refCounter)
        bibList = "${refCounter}. " + bibliography.get(cites) + " <br />"
        replacement = refCounter
      } else {
        replacement = Integer.valueOf(references.get(cites))
      }
      line = line.substring(0, citeStart) + replacement + line.substring(citeEnd+7)
    }
    println line
  }
}
