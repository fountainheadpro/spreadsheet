package dag.calculator

import scala.annotation.tailrec
import scala.collection.mutable
import scala.concurrent.Future
import scala.util.matching.Regex


trait TaskProcessor {

  this: Calculator with DataHolder =>

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit class Regex(sc: StringContext) {
    def r = new scala.util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  @tailrec
  final def dftPath(currPath: mutable.LinkedHashSet[Int], toVisit: mutable.Stack[(Int, Int)]): Unit={
    toVisit match {
      case (currentPos, currentDepth)+:tail=>
        val nextBlock=parseCell(currentPos)
        val nextBlockTraversal = nextBlock.map { pos => (pos, currPath.size + 1) }
        val nextPath: mutable.LinkedHashSet[Int] = if (nextBlockTraversal.nonEmpty){
          if (!currPath.contains(currentPos)) {
            currPath+=currentPos
          }
          else
            throw new IllegalArgumentException(s"Circular dependency detected on path ${currPath.map(input(_)).mkString("->")}")
        } else{
          currPath.take(tail.headOption.map(_._2).getOrElse(0))
        }
        dftPath(nextPath, tail.pushAll(nextBlockTraversal.reverse))
      case _ => Unit
    }
  }

  def parseCell(pos: Int): Vector[Int]={
    val stack = mutable.Stack[Calculatable]()
    val outgoingReferences = mutable.ListBuffer[Int]()
    val cell = input(pos)
    if(Option(calcInProgress(pos)).isEmpty) {
      cell.split(" ").foreach {
        case r"([A-Z])${row}(\d+)${col}" =>
          val refPos = (row.head.toByte - 'A'.toByte) * width + (col.toInt - 1)
          stack.push(Ref(refPos))
          outgoingReferences += refPos
        case r"([+-/*])${operator}" =>
          stack.push(CalcTask(stack.pop, stack.pop, operator))
        case r"(\d*)${value}" => stack.push(Val(value.toDouble))
        case invalid => throw new IllegalArgumentException(s"Invalid input: $invalid")
      }
      calcInProgress(pos) = stack.pop
    } else{
      outgoingReferences ++= parseOnlyRef(cell)
    }
    outgoingReferences.toVector
  }

  def parseOnlyRef(cell: String)=cell.split(" ").collect{
    case r"([A-Z])${row}(\d+)${col}" =>
      (row.head.toByte - 'A'.toByte) * width + (col.toInt - 1)
  }


  def populateCalcInProgress() =
    for(pos <- input.indices){
      val existingCalc = Option(calcInProgress(pos))
      if (existingCalc.isEmpty) {
        dftPath(mutable.LinkedHashSet[Int](),new mutable.Stack().push((pos,0)))
      }
    }

  def executeCalculations() = {
    val cells: Seq[Future[Double]] = calcInProgress.map(_.result.future)
    Future.sequence(cells)
  }

}
