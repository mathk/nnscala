package example

/**
  * Created by msuen on 16/05/17.
  */

import breeze.linalg._
import breeze.numerics._

class Layer(_inputSize: Integer, 
            _outputSize: Integer, 
            _wBiase : Vector[Double],
            _wWeights : Matrix[Double]) {

  protected var wBiase = _wBiase
  protected var wWeights = _wWeights

  def runLayer(input: Vector[Double]): Vector[Double] = 1.0 / (exp(-((wWeights * input) + wBiase)) + 1.0)
  def runDerivativeLayer(input : Vector[Double]) : Vector[Double] = {
    val out = this.runLayer(input)
    return out -:- (out *:* out)
  }

  def backTarget(dedy : Vector[Double]) : Vector[Double] = wWeights.toDenseMatrix.t * dedy.toDenseVector

  def adjust(rate: Double, dedy : Vector[Double], input : Vector[Double]) = {
    wBiase = wBiase -:- (dedy *:* rate)
    wWeights = wWeights.toDenseMatrix -:- ((dedy.toDenseVector * input.toDenseVector.t) *:* rate)
  }

  def isNextCompatible(nextLayer: Layer): Boolean = outputSize == nextLayer.inputSize

  def inputSize = _inputSize

  def outputSize = _outputSize
}


object RandL {
  def apply(inputSize: Integer, outputSize: Integer) =
    new Layer(inputSize,
              outputSize,
              (Vector.rand[Double](outputSize) toDenseVector) *:* 2.0 -:- 1.1,
              (Matrix.rand[Double](outputSize, inputSize) toDenseMatrix) *:* 2.0 -:- 1.0)
}


object L {
  def apply(biase : Vector[Double], weights : Matrix[Double]) = new Layer(weights.rows, biase.length, biase, weights)
}

object LSize {
  def unapply(layer: Layer): Option[(Integer, Integer)] = Some((layer.inputSize, layer.outputSize))
}
