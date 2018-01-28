package life.webapp

import org.scalajs.jquery.jQuery
import scala.scalajs.js
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.{document, CanvasRenderingContext2D}
import model.life.Models._
import model.writers.defaultWriters._

object App {

  var dragging = false

  @js.native
  trait Event extends js.Object {
    val pageX: Int
    val pageY: Int
    val clientX: Int
    val clientY: Int
  }
  val canvasElement = document.getElementById("canvas")
  implicit val canvas: CanvasRenderingContext2D = canvasElement.asInstanceOf[Canvas].getContext("2d").asInstanceOf[CanvasRenderingContext2D]

  def main(args: Array[String]): Unit = {
    val body = jQuery("body")

    cellGrid.draw
    jQuery("#update").click(() => updateGrid)
    jQuery("#canvas").click((e: js.Any) => {dealWithClick(e.asInstanceOf[Event])})
    jQuery("#canvas").mousedown((_) => startDrag).mouseup((_) => stopDrag).mousemove((e) => drag(e.asInstanceOf[Event]))
  }

  def updateGrid = {
    js.timers.setInterval(20) {
      cellGrid.update
      cellGrid.draw
    }
  }

  def startDrag = {
    dragging = true
  }

  def stopDrag = {
    dragging = false
  }

  def drag(event: Event) = {
    if (dragging) dealWithClick(event, true)
  }

  def dealWithClick(event: Event, asDrag: Boolean = false) = {
    val (x, y) = (event.clientX - canvasElement.getBoundingClientRect().left.toInt , event.clientY - canvasElement.getBoundingClientRect().top.toInt)
    cellGrid.toggleCell(x, y, asDrag)
    cellGrid.draw
  }
}