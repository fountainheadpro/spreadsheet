package dag.calculator

import scala.concurrent.Promise

/**
  * Created by szelvenskiy on 3/18/16.
  */


trait Calculatable {
  val result: Promise[Double]
}
