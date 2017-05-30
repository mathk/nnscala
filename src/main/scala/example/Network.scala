package example

/**
  * Created by msuen on 17/05/17.
  */

import breeze.linalg._

import scala.collection.mutable._
import scala.util.{Failure, Success, Try}

trait Network {
  import example.Type._
  def <~ (input : Vector[Double]) : Vector[Double]

  def <~~ (input : List[Vector[Double]]) : List[Vector[Double]] = {
    input.map({this <~ _})
  }

  def meanSquaredError(inputs : TrainSet) : Double = {
    sum(for (
      i <- inputs
    ) yield {
      val v = (i._2 -:- (this <~ i._1)) 
      sum(v * v)
    })
  }

  def trainSet(rate: Double, inputs : TrainSet) = {
    inputs map {case (input, target) => this.train(rate, input, target)}
  }
  def train(rate: Double, input : Vector[Double], target : Vector[Double]) : Network = this.trainStep(rate, input, target)._1

  def trainStep(rate: Double, input : Vector[Double], target : Vector[Double]) : (Network, Vector[Double])
}

case class InnerLayer(layer: Layer, net : Network) extends Network {
  def <~ (input: Vector[Double]) : Vector[Double] = net <~ layer.runLayer(input)
  def trainStep(rate: Double, input : Vector[Double], target : Vector[Double]) : (Network, Vector[Double]) = {
    val nextInput = layer.runLayer(input)
    val targetProp = net.trainStep(rate, nextInput, target)._2
    val dedy = layer.runDerivativeLayer(input) *:* targetProp
    val nextTarget = layer.backTarget(dedy)
    layer.adjust(rate, dedy, input)
    return (this, nextTarget)
  }
}
case class OutputLayer(layer: Layer) extends Network {
  def <~ (input: Vector[Double]) : Vector[Double] = layer.runLayer(input)
  def trainStep(rate : Double, input: Vector[Double], target : Vector[Double]) : (Network, Vector[Double]) = {
    val dedy = (layer.runLayer(input) -:- target) *:* layer.runDerivativeLayer(input)
    val nextTarget = layer.backTarget(dedy)
    layer.adjust(rate, dedy, input)
    return (this, nextTarget)
  }
}

object N {
  implicit def layerContruction(layer: Layer) = new {
    def ~>(network : Network) : Network = new InnerLayer(layer, network)
  }

  implicit def outputLayerContruction(layer : Layer) = new {
    def O : Network = new OutputLayer(layer)
  }
}

