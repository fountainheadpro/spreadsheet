package dag.calculator

import scala.concurrent.duration.Duration
import scala.concurrent.Await
import scala.util.Try


object Main extends App with TaskProcessor with Calculator with DataHolder {

  val (width: Int, height: Int) = Try {
    readLine().split(" ").map(_.toInt) match {
      case Array(w: Int, h: Int) => (w, h)
      case _ => throw new IllegalArgumentException("First line does not have valid width and height")
    }
  }.recoverWith{
    case ex:Exception => println("Error parsing width and height", ex)
    sys.exit(-1)
  }.get

  val input = Array.fill(height * width)(readLine)
  val calcInProgress = new Array[Calculatable](input.length)

  populateCalcInProgress()

  val finalRes = Await.result(executeCalculations(), Duration.Inf)

  println(s"$width $height")
  finalRes.foreach{v=>println(v.formatted("%.5f"))}

}
