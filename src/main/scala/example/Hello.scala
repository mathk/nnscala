package example

import breeze.linalg._

import scala.util._
import example.N._

object Hello extends App {
  val net = RandL(10, 3) ~> RandL(3, 1).O
  println(net <~ Vector[Double](1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0))

  val l = RandL(1, 3)
  println(l.runLayer(Vector[Double](1.0)))
  println(l.runDerivativeLayer(Vector[Double](1.0)))

  val n = RandL(2, 4) ~> RandL(4, 1).O
  val s = TrainSetParser.parseFile("./sample.txt").get
  val cs = TrainSetParser.parseFile("./canonicsample.txt").get
  val sc = (for (
    i <- 1 to 30000
  ) yield { DenseVector.rand[Double](2)}).map({ v => 
    if (v(0) < 0.5 && v(1) < 0.5) (v, DenseVector[Double](0.0))
    else if (v(0) < 0.5 && v(1) >= 0.5) (v, DenseVector[Double](1.0))
    else if (v(0) >= 0.5 && v(1) < 0.5) (v, DenseVector[Double](1.0))
    else (v, DenseVector[Double](0.0))
    }).toList
}
