import groovy.json.JsonSlurper
def slurper = new JsonSlurper()

//def jobsJson = new JsonSlurper().parseText( new URL( 'https://api.github.com/repos/chughs/spark/git/trees/master?' ).text )
//println jobsJson
def giturl = "http://github.com/chughs/spark.git"
def branch = "master"
def command = "-e clean deploy -DskipTests"
def jobsJson = slurper.parseText(readFileFromWorkspace("test.json"))

jobsJson.projects.each { team,APM -> 
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
/*if (it.type == 'tree' && it.path != '.github') {
   path = it.path
   println "Creating jobs " + path
mavenJob("APM-${it.path}") {
    scm {
        git(giturl,branch)
    }
    triggers {
    }
        rootPOM("${path}/pom.xml")
        goals(command)
    /*steps {
        maven(command)
    }
}
}
}*/
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
