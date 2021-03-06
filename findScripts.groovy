// Copyright (c) 2011-2018  Egon Willighagen <egon.willighagen@gmail.com>
//
// GPL v3

// find all references to scripts
//
// it takes one optional argument, which is appended to the output

if (args.length == 0) {
  println "groovy findScripts.groovy <directory> [suffix]"
  System.exit(0)
}

def folder = args[0]

def suffix = ""
if (args.length == 2) suffix = args[1]

def basedir = new File(folder)
files = basedir.listFiles().grep(~/.*i.md$/)
files.each { file ->
  file.eachLine { line ->
    if (line.contains("<code>")) {
      def instruction = new XmlSlurper().parseText(line)
      println "" + instruction.text() + suffix
    } else if (line.contains("<out>")) {
      def instruction = new XmlSlurper().parseText(line)
      println "" + instruction.text() + suffix
    }
  }
}

