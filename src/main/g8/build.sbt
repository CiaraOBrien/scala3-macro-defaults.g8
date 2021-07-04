val versionString = "3.0.0"

lazy val $name;format="camel"$ = project.in(file("."))
.settings(  
  name                 := "$name;format="normalize"$",
  version              := "0.1.0",
  scalaVersion         := versionString,
  organization         := "$organization$",
  libraryDependencies ++= Seq(
    $if(tests.truthy)$
     "org.scalameta" %% "munit" % "0.7.27" % Test,
    $endif$
  ),
  $if(tests.truthy)$
  testFrameworks    += new TestFramework("munit.Framework"),
  $endif$
  scalacOptions    ++= Seq(
    "-indent", "-new-syntax",
    "-Yexplicit-nulls", 
    "-language:strictEquality", 
  ),
  Compile / scalaSource       := baseDirectory.value / "src" / "main",
  Test    / scalaSource       := baseDirectory.value / "src" / "test",
  Compile / javaSource        := baseDirectory.value / "src" / "main",
  Test    / javaSource        := baseDirectory.value / "src" / "test",
  Compile / resourceDirectory := baseDirectory.value / "src" / "res",
  Test    / resourceDirectory := baseDirectory.value / "src" / "res",
)

$if(docs.truthy)$
lazy val docs = project.in(file("src") / "docs") 
  .dependsOn($name;format="camel"$)
  .enablePlugins(MdocPlugin)
$endif$

$if(playground.truthy)$
val taste         = taskKey[Unit]("Compile and run \"tasty\", the playground subproject, printing the AST at certain phases.")
// Names of useful compiler phases
val TyperPhase    = "typer"
val MacrosPhase   = "staging"
val ErasurePhase  = "erasure"
val LastPhase     = "collectSuperCalls"
val BytecodePhase = "genBCode"
// List of phases to print
val PhasesToPrint   = Seq(macrosPhase)

lazy val tasty = project.in(file("tasty"))
.dependsOn($name;format="camel"$)
.settings(
  name := "tasty-playground",
  scalaVersion := versionString,
  Compile / scalaSource      := (ThisBuild / baseDirectory).value / "tasty",
  Test    / unmanagedSources := Nil,
  Compile / logBuffered      := true,
  Compile / scalacOptions    += ("-Xprint:" + printPhases.mkString(",")),
  Compile / taste := Def.sequential(
    $name;format="camel"$ / Compile / compile,
    Compile / clean,
    (Compile / run).toTask("")
  ).value,
  publish := {}, publishLocal := {}, test := {}, doc := { file(".") }
)
$endif$
