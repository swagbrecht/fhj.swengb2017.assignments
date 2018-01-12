package at.fhj.swengb.apps.calculator

import java.net.URL
import java.util.ResourceBundle
import javafx.application.Application
import javafx.beans.property.{ObjectProperty, SimpleObjectProperty}
import javafx.event.Event
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.control.{TextField, Button}
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import scala.util.{Failure, Success}
import scala.util.control.NonFatal
import scala.util.matching.Regex

object CalculatorApp {

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[CalculatorFX], args: _*)
  }
}

class CalculatorFX extends javafx.application.Application {

  val fxml = "/at/fhj/swengb/apps/calculator/calculator.fxml"
  val css = "/at/fhj/swengb/apps/calculator/calculator.css"

  def mkFxmlLoader(fxml: String): FXMLLoader = {
    new FXMLLoader(getClass.getResource(fxml))
  }

  override def start(stage: Stage): Unit =
    try {
      stage.setTitle("Calculator")
      setSkin(stage, fxml, css)
      stage.show()
      stage.setMinWidth(stage.getWidth)
      stage.setMinHeight(stage.getHeight)
    } catch {
      case NonFatal(e) => e.printStackTrace()
    }

  def setSkin(stage: Stage, fxml: String, css: String): Boolean = {
    val scene = new Scene(mkFxmlLoader(fxml).load[Parent]())
    stage.setScene(scene)
    stage.getScene.getStylesheets.clear()
    stage.getScene.getStylesheets.add(css)
  }

}

class CalculatorFxController extends Initializable {

  val calculatorProperty: ObjectProperty[RpnCalculator] = new SimpleObjectProperty[RpnCalculator](RpnCalculator())

  val isNumber = "([0-9])".r

  var isCalculated = false

  def getCalculator() : RpnCalculator = calculatorProperty.get()

  def setCalculator(rpnCalculator : RpnCalculator) : Unit = calculatorProperty.set(rpnCalculator)

  @FXML var numberTextField : TextField = _

  override def initialize(location: URL, resources: ResourceBundle) = {

  }

  def sgn(): Unit = {
    op(numberTextField.getText)
    getCalculator().stack foreach println
  }

  def op(s: String): Unit = getCalculator().push(Op(s)) match {
    case Success(c) => setSuccess(c)
    case Failure(e) => setError()
  }

  def inp(e: Event): Unit = e.getSource.asInstanceOf[Button].getText match {
    case isNumber(num) => if (isErr || isCalculated || isZero) setNumberTextFieldText(num) else appendNumberTextFieldText(num)
    case "add" => op("+")
    case "subtract" => op("-")
    case "multiply" => op("*")
    case "divide" => op("/")
    case "point" => if (!isErr && !hasPoint) appendNumberTextFieldText(".")
    case "+/-" => if (!isErr && !isZero) invertNumberTextFieldNumber()
    case "clear" => if (isZero) setCalculator(RpnCalculator()) else setNumberTextFieldText("0")
  }

  private def setNumberTextFieldText(s: String): Unit = {numberTextField.setText(s); isCalculated = false}

  private def appendNumberTextFieldText(s: String): Unit = numberTextField.setText(numberTextField.getText + s)

  private def invertNumberTextFieldNumber(): Unit = numberTextField.setText((numberTextField.getText.toDouble * -1).toString)

  private def setError(): Unit = setNumberTextFieldText("err")

  private def setSuccess(c: RpnCalculator): Unit = c.peek match {
    case v: Val => setCalculator(c); numberTextField.setText(v.value.toString); isCalculated = true
    case _ =>
  }

  private def isErr() = numberTextField.getText == "err"

  private def isZero() = numberTextField.getText == "0"

  private def hasPoint() = numberTextField.getText.contains(".")

}