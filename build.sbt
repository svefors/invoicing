import Dependencies._

import com.typesafe.sbt.SbtGit._
import releasenotes._

versionWithGit

showCurrentGitBranch

lazy val baseName = "invoicing"

version := "1.0"

lazy val accounts = project
  .settings(
    normalizedName := s"$baseName-${normalizedName.value}",
    libraryDependencies ++= Seq(
      scalazCore,
      jodaTime,
      jodaConvert,
      jodaMoney,
      scalaTest,
      akkaContrib))


organization in ThisBuild := "sweforce.invoicing"

scalaVersion in ThisBuild := "2.11.0"
