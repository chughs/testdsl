import groovy.json.JsonSlurper
def slurper = new JsonSlurper()

//def jobsJson = new JsonSlurper().parseText( new URL( 'https://api.github.com/repos/chughs/spark/git/trees/master?' ).text )
//println jobsJson
def giturl = "http://github.com/chughs/spark.git"
def branch = "master"
def command = "-e clean deploy -DskipTests"
def jobsJson = slurper.parseText(readFileFromWorkspace("test.json"))

jobsJson.APM.each { team,APM -> 
     jobnames=APM
     folder(team) {
         displayName(team)
             description('Folder for APM')
             }

     println "created folder" + team
     jobnames.each { jobs,config ->
         println "Jobs name is " + jobs
         println "config for jobs is " + config
         mavenJob("${team}/${jobs}") {
            scm {
              git(config.giturl,config.branch)
            }
            triggers {
            }
             goals(config.buildscript)
   }
}
}
sectionedView('APM') {
    sections {
        listView {
            name('APM')
            width('HALF')
            alignment('LEFT')
            jobs {
                regex('A-.*')
            }
            columns {
                status()
                weather()
                name()
                lastSuccess()
                lastFailure()
            }
        }
    }
}
