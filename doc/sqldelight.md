Gradle
![image info](./How2UseSqlDelight.png)
For greater customization, you can declare databases explicitly using the Gradle DSL.

build.gradle:

``` gradle 
sqldelight {
// Database name
MyDatabase {
// Package name used for the generated MyDatabase.kt
packageName = "com.example.db"

    // An array of folders where the plugin will read your '.sq' and '.sqm' 
    // files. The folders are relative to the existing source set so if you
    // specify ["db"], the plugin will look into 'src/main/db' or 'src/commonMain/db' for KMM. 
    // Defaults to ["sqldelight"]
    sourceFolders = ["db"]

    // The directory where to store '.db' schema files relative to the root 
    // of the project. These files are used to verify that migrations yield 
    // a database with the latest schema. Defaults to null so the verification 
    // tasks will not be created.
    schemaOutputDirectory = file("src/main/sqldelight/databases")

    // Optionally specify schema dependencies on other gradle projects
    dependency project(':OtherProject')

    // The dialect version you would like to target
    // Defaults to "sqlite:3.18"
    dialect = "sqlite:3.24"

    // If set to true, migration files will fail during compilation if there are errors in them.
    // Defaults to false
    verifyMigrations = true
}
// For native targets, whether sqlite should be automatically linked.
// Defaults to true.
linkSqlite = false
}

```

If you're using Kotlin for your Gradle files:

build.gradle.kts

``` gradle 
sqldelight {
database("MyDatabase") {
packageName = "com.example.db"
sourceFolders = listOf("db")
schemaOutputDirectory = file("build/dbs")
dependency(project(":OtherProject"))
dialect = "sqlite:3.24"
verifyMigrations = true
}
linkSqlite = false
}
```
Dependencies

You can specify schema dependencies on another module:

```
sqldelight {
MyDatabase {
package = "com.example.projecta"
dependency project(":ProjectB")
}
}
```
This looks for MyDatabase in ProjectB and includes it's schema when compiling. For this to work, ProjectB must have a database with the same name (MyDatabase in this case) but generate in a different package, so here is what ProjectB's gradle might look like:

```
sqldelight {
MyDatabase {
package = "com.example.projectb"
}
}
```
