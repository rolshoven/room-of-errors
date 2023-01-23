plugins {
  val kotlin = "1.7.21"
  val ktor = "2.2.2"
  val flyway = "9.8.2"
  val jooq = "8.0"
  kotlin("multiplatform") version kotlin
  kotlin("plugin.serialization") version kotlin
  id("org.flywaydb.flyway") version flyway
  id("nu.studer.jooq") version jooq
  id("io.ktor.plugin") version ktor
  application
}

group = "com.fynnian"
version = "1.0-SNAPSHOT"

object Versions {
  // https://github.com/JetBrains/kotlin-wrappers
  const val wrapperBom = "1.0.0-pre.453"
  const val serializationJson = "1.4.1"
  const val coroutines = "1.6.4"
  const val ktor = "2.2.2"
  const val logback = "1.2.11"
  const val multiplatformUUID = "0.6.0"
  const val hikari = "5.0.1"
  const val h2 = "2.1.214"
  const val jooq = "3.17.5"
  const val flyway = "9.8.2"
}

repositories {
  mavenCentral()
  maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

dependencies {
  jooqGenerator("com.h2database:h2:${Versions.h2}")
}

kotlin {
  jvm {
    compilations.all {
      kotlinOptions.jvmTarget = "17"
    }
    withJava()
    testRuns["test"].executionTask.configure {
      useJUnitPlatform()
    }
  }
  js(IR) {
    binaries.executable()
    browser {
      commonWebpackConfig {
        cssSupport.enabled = true
        devServer?.open = false
        devServer?.port = 9090

      }
    }
  }
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation("io.ktor:ktor-client-core:${Versions.ktor}")
        implementation("io.ktor:ktor-client-content-negotiation:${Versions.ktor}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serializationJson}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")

        // global uuid
        implementation("com.benasher44:uuid:${Versions.multiplatformUUID}")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    val jvmMain by getting {
      dependencies {
        implementation("io.ktor:ktor-server-content-negotiation:${Versions.ktor}")
        implementation("io.ktor:ktor-server-cors:${Versions.ktor}")
        implementation("io.ktor:ktor-server-compression:${Versions.ktor}")
        implementation("io.ktor:ktor-server-core-jvm:${Versions.ktor}")
        implementation("io.ktor:ktor-server-netty:${Versions.ktor}")
        implementation("io.ktor:ktor-server-status-pages:${Versions.ktor}")
        implementation("io.ktor:ktor-client-cio:${Versions.ktor}")
        implementation("io.ktor:ktor-serialization:${Versions.ktor}")
        implementation("io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}")
        implementation("ch.qos.logback:logback-classic:${Versions.logback}")
        implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")

        // persistence
        implementation("com.zaxxer:HikariCP:${Versions.hikari}")
        implementation("com.h2database:h2:${Versions.h2}")
        implementation("org.jooq:jooq:${Versions.jooq}")
        implementation("org.flywaydb:flyway-core:${Versions.flyway}")

      }
    }
    val jvmTest by getting {

    }
    val jsMain by getting {
      dependencies {
        implementation("io.ktor:ktor-client-js:${Versions.ktor}")
        implementation("io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}")

        implementation(project.dependencies.enforcedPlatform(kotlinw("wrappers-bom:${Versions.wrapperBom}")))
        implementation(kotlinw("react"))
        implementation(kotlinw("react-dom"))
        implementation(kotlinw("react-router-dom"))
        implementation(kotlinw("emotion"))
        implementation(kotlinw("mui"))
        implementation(kotlinw("mui-icons"))
      }
    }
    val jsTest by getting {

    }
  }
}
fun kotlinw(target: String): String =
  "org.jetbrains.kotlin-wrappers:kotlin-$target"

application {
  mainClass.set("com.fynnian.application.ServerKt")
}

val dbDriver = "org.h2.Driver"
val dbUrl = "jdbc:h2:file:${project.buildDir}/flyway/horrors_db"
val dbUser = "test"
val dbPassword = "test"
val dbSchema = "room_of_horrors"

flyway {
  url = dbUrl
  user = dbUser
  password = dbPassword
  schemas = arrayOf(dbSchema)
  locations = arrayOf(
    "filesystem:${project.kotlin.sourceSets["jvmMain"].resources.srcDirs.first()}/db/migration"
  )
  cleanDisabled = false
}


jooq {
  configurations {
    create("main") {
      jooqConfiguration.apply {
        jdbc.apply {
          driver = dbDriver
          url = dbUrl
          user = dbUser
          password = dbPassword
        }
        generator.apply {
          name = "org.jooq.codegen.DefaultGenerator"
          database.apply {
            name = "org.jooq.meta.h2.H2Database"
            inputSchema = dbSchema
            excludes = """
              flyway_schema_history
            """.trimIndent()
            // keep force type block for reference
            // forcedTypes.addAll(listOf(
            //   org.jooq.meta.jaxb.ForcedType().apply {
            //     userType = "com.fynnian.application.common.domain.Coordinates"
            //     converter = "com.fynnian.application.jooq.CoordinatesConverter"
            //     includeExpression = ".*\\.coordinates.*"
            //   }
            // ))
          }
          generate.apply {
            isDeprecated = false
            isRecords = true
            isImmutablePojos = true
            isFluentSetters = true
          }
          target.apply {
            packageName = "com.fynnian.application.jooq"
            directory = "build/generated-src/jooq/main"
          }
          strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
        }
      }
    }
  }
}

tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
  dependsOn(tasks.getByName<org.flywaydb.gradle.task.FlywayMigrateTask>("flywayMigrate"))

  // declare Flyway migration scripts as inputs on the jOOQ task
  inputs.files(project.kotlin.sourceSets["jvmMain"].resources.asFileTree)
    .withPropertyName("migrations")
    .withPathSensitivity(PathSensitivity.RELATIVE)

  allInputsDeclared.set(true)
}

// include JS artifacts on production builds JAR we generate
tasks.getByName<Jar>("jvmJar") {
  if (project.hasProperty("isProduction") || project.gradle.startParameter.taskNames.contains("installDist")) {
    val webpackTask =
      tasks.getByName<org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack>("jsBrowserProductionWebpack")
    dependsOn(webpackTask) // make sure JS gets compiled first
    from(File(webpackTask.destinationDirectory, webpackTask.outputFileName)) // bring output file along into the JAR
  }
}

tasks.getByName<JavaExec>("run") {
  classpath(tasks.getByName<Jar>("jvmJar")) // so that the JS artifacts generated by `jvmJar` can be found and served
}

// resolve gradle warning for depending task
tasks.getByName("jsBrowserDevelopmentRun") {
  dependsOn(tasks.getByName("jsDevelopmentExecutableCompileSync"))
}

// workaround for hardcoded webpack version 4.9 that has a bug
// https://stackoverflow.com/a/72731728/12381648
rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
  versions.webpackCli.version = "4.10.0"
  versions.webpackDevServer.version = "4.0.0"
}