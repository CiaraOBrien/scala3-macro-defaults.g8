lazy val $name;format="camel"$ = project.in(file("."))
.settings(  
  name                 := "$name;format="normalize"$",
  version              := "0.1.0",
  scalaVersion         := "3.0.0-M3",
  organization         := "$organization$",
  libraryDependencies ++= Seq(
    $if(cats.truthy)$
     "org.typelevel" %% "cats-core" % "2.3.1",
    $endif$
    $if(minitest.truthy)$
     "io.monix"      %% "minitest"  % "2.9.2" % "test",
    $endif$
  ),
  $if(minitest.truthy)$
  testFrameworks    += new TestFramework("minitest.runner.Framework"),
  $endif$
  scalacOptions    ++= Seq(
    "-source:3.1", "-indent", "-new-syntax",
    "-Yexplicit-nulls", "-Ycheck-init", "-Yerased-terms",
    "-language:strictEquality", 
  ),
  Compile / scalaSource       := baseDirectory.value / "src" / "main",
  Test    / scalaSource       := baseDirectory.value / "src" / "test",
  Compile / javaSource        := baseDirectory.value / "src" / "main",
  Test    / javaSource        := baseDirectory.value / "src" / "test",
  Compile / resourceDirectory := baseDirectory.value / "src" / "res",
  Test    / resourceDirectory := baseDirectory.value / "src" / "res",
)

$if(playground.truthy)$
val typerPhase    = "typer"
val macrosPhase   = "staging"
val erasurePhase  = "erasure"
val lastPhase     = "collectSuperCalls"
val bytecodePhase = "genBCode"
val printPhases   = Seq(macrosPhase)
val taste         = taskKey[Unit]("Clean and run \"tasty\", the playground subproject")

lazy val tasty = project.in(file("tasty"))
.dependsOn($name;format="camel"$)
.settings(
  name := "tasty-playground",
  scalaVersion := "3.0.0-M3",
  Compile / scalaSource      := (ThisBuild / baseDirectory).value / "tasty",
  Test    / unmanagedSources := Nil,
  Compile / logBuffered      := true,
  Compile / scalacOptions    += ("-Xprint:" + printPhases.mkString(",")),
  Compile / taste := Def.sequential(
    dottytags.jvm / Compile / compile,
    Compile / clean,
    (Compile / run).toTask("")
  ).value,
  publish := {}, publishLocal := {}, test := {}, doc := { file(".") }
)
$endif$