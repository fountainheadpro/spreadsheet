package dag.calculator

/**
  * Created by szelvenskiy on 3/19/16.
  */
trait DataHolder {

  val width: Int
  val height: Int

  val input: Array[String]

  val calcInProgress: Array[Calculatable]

}


