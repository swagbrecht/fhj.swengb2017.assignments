package at.fhj.swengb.apps.calculator

import java.nio.file.{Files, Path, Paths}

import org.scalatest.WordSpecLike

import scala.collection.JavaConverters._

class TimesheetSpec extends WordSpecLike {
  val content = "asdf"
  val condition: Boolean = ???

  val p: Path = Paths.get("C:\\Workspace\\fhj.swengb2017.assignments\\calculator\\timesheet-calculator.adoc")

  "timesheet-calculator" should {
    "not be the same like content" in {
      // we need code for reading the file
      val alleZeilenDieserDatei = Files.readAllLines(p)
      println("datei hat " + alleZeilenDieserDatei.size + " zeilen.")
      //we need code to compare the file

      assert(condition)
    }
  }
}
