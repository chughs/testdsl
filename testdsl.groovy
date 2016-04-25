import groovy.json.JsonSlurper
def slurper = new JsonSlurper()

def jobsJson = new JsonSlurper().parseText( new URL( 'https://api.github.com/repos/chughs/spark/git/trees/master?' ).text )
//println jobsJson
def giturl = "http://github.com/chughs/spark.git"
def branch = "master"
def command = "-e clean deploy -DskipTests"
jobsJson.tree.each { 
if (it.type == 'tree' && it.path != '.github') {
   path = it.path
   println "Creating jobs " + path
mavenJob("APM-${it.path}") {
    scm {
        git(giturl,branch)
    }
    triggers {
     scm('*/15 * * * *')
    }
        rootPOM("${path}/pom.xml")
        goals(command)
    /*steps {
        maven(command)
    }*/
}
}
}
categorizedJobsView('APM-Jobs') {
    jobs {
        regex(/APM-.*/)
    }
    categorizationCriteria {
        regexGroupingRule(/^APM-.*$/, namingRule="APM-Master")

    }
    description("APM-Master")
    columns {
        status()
        categorizedJob()
        buildButton()
    }
}
