import Dependencies._

import com.typesafe.sbt.SbtGit._
import releasenotes._

versionWithGit

showCurrentGitBranch

git.baseVersion := "0.5.0"

lazy val baseName = "invoicing"

organization in ThisBuild := "sweforce.invoicing"

scalaVersion in ThisBuild := "2.10.4"

lazy val accounts = project
  .settings(
  normalizedName := s"$baseName-${normalizedName.value}",
  libraryDependencies ++= Seq(
  scalazCore,
  jodaTime,
  jodaConvert,
  jodaMoney,
  scalaTest,
  akkaContrib)
  )

sourcesInBase := false