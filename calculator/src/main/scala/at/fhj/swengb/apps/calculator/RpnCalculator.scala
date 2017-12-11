package at.fhj.swengb.apps.calculator

import java.util.NoSuchElementException

import scala.util.{Failure, Success, Try}

/**
  * Companion object for our reverse polish notation calculator.
  */
object RpnCalculator {

  /**
    * Returns empty RpnCalculator if string is empty, otherwise pushes all operations
    * on the stack of the empty RpnCalculator.
    *
    * @param s a string representing a calculation, for example '1 2 +'
    * @return
    */
  def apply(s: String): Try[RpnCalculator] = {
    def _apply(op: List[Op], calc: RpnCalculator): Try[RpnCalculator] = op match {
      case Nil => Success(calc)
      case head::Nil => calc.push(head)
      case head::tail => calc.push(head) match {
        case Success(c) => _apply(tail, c)
        case Failure(e) => Failure(e)
      }
    }

    if (s == "") { return Success(RpnCalculator()) }

    _apply(s.split(" ").map(Op(_)).toList, RpnCalculator())
  }

}

/**
  * Reverse Polish Notation Calculator.
  *
  * @param stack a datastructure holding all operations
  */
case class RpnCalculator(stack: List[Op] = Nil) {

  /**
    * By pushing Op on the stack, the Op is potentially executed. If it is a Val, it the op instance is just put on the
    * stack, if not then the stack is examined and the correct operation is performed.
    *
    * @param op
    * @return
    */
  def push(op: Op): Try[RpnCalculator] = try {
    op match {
      case v: Val => Success(RpnCalculator(v :: stack))
      case Mul => pop()._2.pop()._2.push(Mul.eval(pop()._1.asInstanceOf[Val], pop()._2.pop()._1.asInstanceOf[Val]))
      case Sub => pop()._2.pop()._2.push(Sub.eval(pop()._1.asInstanceOf[Val], pop()._2.pop()._1.asInstanceOf[Val]))
      case Add => pop()._2.pop()._2.push(Add.eval(pop()._1.asInstanceOf[Val], pop()._2.pop()._1.asInstanceOf[Val]))
      case Div => pop()._2.pop()._2.push(Div.eval(pop()._1.asInstanceOf[Val], pop()._2.pop()._1.asInstanceOf[Val]))
    }
  } catch { case e: NoSuchElementException => Failure(e) }

  /**
    * Pushes val's on the stack.
    *
    * If op is not a val, pop two numbers from the stack and apply the operation.
    *
    * @param ops
    * @return
    */
  def push(ops: Seq[Op]): Try[RpnCalculator] = ops match {
    case Nil => Success(this)
    case head::Nil => push(head)
    case head::tail => push(head) match {
      case Success(c) => c.push(tail)
      case Failure(e) => Failure(e)
    }
  }


  /**
    * Returns an tuple of Op and a RevPolCal instance with the remainder of the stack.
    *
    * @return
    */
  def pop(): (Op, RpnCalculator) = (peek(), RpnCalculator(stack.reverse.drop(1).reverse))

  /**
    * If stack is nonempty, returns the top of the stack. If it is empty, this function throws a NoSuchElementException.
    *
    * @return
    */
  def peek(): Op = if (stack.nonEmpty) stack.last else throw new NoSuchElementException

  /**
    * returns the size of the stack.
    *
    * @return
    */
  def size: Int = stack.length
}