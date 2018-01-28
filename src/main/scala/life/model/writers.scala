package model.writers

import org.scalajs.jquery.jQuery
import model.life.{Cell, EmptyCell, FullCell, CellGrid}
import org.scalajs.dom.CanvasRenderingContext2D

trait GridWriter {
  def write(g: CellGrid, canvas: CanvasRenderingContext2D): Unit
  def cellSize: Int
  def cellGap: Int
}

object defaultWriters {

  implicit object defaultWriter extends GridWriter {
    val cellSize = 7
    val cellGap = 1
    def write(g: CellGrid, canvas: CanvasRenderingContext2D): Unit = {
      g.grid.foreach( (pair) => {
        pair match {
          case ((x, y), EmptyCell) => canvas.fillStyle = "#000"; canvas.fillRect(x * (cellSize + cellGap), y * (cellSize + cellGap), cellSize, cellSize)
          case((x, y), FullCell) => canvas.fillStyle = "#ff0000"; canvas.fillRect(x * (cellSize + cellGap), y * (cellSize + cellGap), cellSize, cellSize)
          case (_,_) => Unit
        }
      } )
    }
  }
}