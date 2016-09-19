package dag.calculator

import scala.concurrent.Promise




trait Calculator {

  this: DataHolder =>

  import scala.concurrent.ExecutionContext.Implicits.global


  case class Val(value: Double) extends Calculatable {
    val result = Promise.successful(value) //{val p=Promise[Double]; p.success(value); p.future}
  }

  case class Ref(pos: Int) extends Calculatable {

    lazy val result = Option(calcInProgress(pos)) match{
      case Some(calc) => calc.result
      case None => throw new IllegalStateException(s"Reference is not defined for $pos")
    }

  }

  abstract class CalcTask(left: Calculatable, right: Calculatable) extends Calculatable {

    lazy val result: Promise[Double] = {
      val f = for (
        leftResult <- left.result.future;
        rightResult <- right.result.future
      ) yield op(leftResult, rightResult)
      Promise[Double].completeWith(f)
    }

    def op(l: Double, right: Double): Double

  }

  case class Add(left: Calculatable, right: Calculatable) extends CalcTask(left, right) {
    def op(l: Double, r: Double): Double = l + r
  }

  case class Substruct(left: Calculatable, right: Calculatable) extends CalcTask(left, right) {
    def op(l: Double, r: Double): Double = l - r
  }

  case class Multiply(left: Calculatable, right: Calculatable) extends CalcTask(left, right) {
    def op(l: Double, r: Double): Double = l * r
  }

  case class Divide(left: Calculatable, right: Calculatable) extends CalcTask(left, right) {
    def op(l: Double, r: Double): Double = l / r
  }

  object CalcTask {

    def apply(left: Calculatable, right: Calculatable, op: String) = op match {
      case "+" => Add(left, right)
      case "-" => Substruct(right, left)
      case "*" => Multiply(left, right)
      case "/" => Divide(right, left)
    }

  }

}
