package example

import breeze.linalg._
object Type {
  type TrainInput = Tuple2[Vector[Double], Vector[Double]]
  type TrainSet = List[TrainInput]
}
