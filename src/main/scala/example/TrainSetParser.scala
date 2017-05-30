package example

import scala.util.parsing.combinator._
import breeze.linalg._
import scala.util._
import scala.io.Source

object TrainParsers extends JavaTokenParsers {

  import example.Type._

  def number: Parser[Double] = floatingPointNumber ^^ { _.toDouble }
  def vector : Parser[Vector[Double]] = "(" ~> repsep(number, ",") <~ ")" ^^ { l => DenseVector(l.toArray) }
  def inputTuple : Parser[TrainInput] = vector ~ "->" ~ vector ^^ { case l ~ _ ~ r => (l,r) }

  def trainSet : Parser[TrainSet] = rep(inputTuple)

}

object TrainSetParser {
  import example.Type._
  def parseFile(fileName : String) : Try[TrainSet] = {
    TrainParsers.parse(TrainParsers.trainSet, Source.fromFile(fileName).reader()) match {
      case TrainParsers.Success(elem, _) => new Success(elem)
      case _ => new Failure(new  Exception("Can not parse input"))
    }
  }
}
