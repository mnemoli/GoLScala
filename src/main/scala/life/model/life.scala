package model.life

import model.writers
import org.scalajs.dom.CanvasRenderingContext2D

import scala.collection.parallel.ParMap
import scala.collection.mutable.HashMap

trait Cell
case object EmptyCell extends Cell
case object FullCell extends Cell

class CellGrid(gridSize: Int) {

  var grid = new HashMap[Tuple2[Int, Int], Cell with Product with Serializable]
  var lastUpdatedCellLoc = (-1,-1)

  for (r <- 0 until gridSize; c <- 0 until gridSize) {
    grid((r, c)) = EmptyCell
  }

  def draw[A](implicit writer: writers.GridWriter, canvas: CanvasRenderingContext2D): Unit = {
    writer.write(this, canvas)
  }

  def update: Unit = {

    grid = grid.map( pair => {
      val (thisx, thisy) = pair._1
      val neighbours = grid.filter(k => k match {
        case ((x, y), FullCell) if ((thisx-1 to thisx+1 contains x) && (thisy-1 to thisy+1 contains y) && (x,y) != (thisx, thisy)) => true
        case _ => false
      }  )
      (pair, neighbours.size) match {
        case (_, 3) => (pair._1, FullCell) // a cell is 'born' or survives if it has exactly 3 neighbours
        case (((_,_), FullCell), 2) => (pair._1, FullCell) // a living cell survives if it has 2 neighbours
        case _ => (pair._1, EmptyCell) // otherwise a cell dies or remains dead
      }
    })
  }

  def toggleCell(clickX: Int, clickY: Int, preventToggleLast: Boolean = false)(implicit writer: writers.GridWriter) = {
    val cellLoc = (clickX / (writer.cellSize + writer.cellGap), clickY / (writer.cellSize + writer.cellGap))

    if (!preventToggleLast || cellLoc != lastUpdatedCellLoc || lastUpdatedCellLoc == (-1,-1)) {
      grid(cellLoc) = grid(cellLoc) match {
        case FullCell => EmptyCell
        case EmptyCell => FullCell
      }
      lastUpdatedCellLoc = cellLoc
    }

  }
}
object CellGrid {
  def apply(size: Int) = {
    new CellGrid(size)
  }
}

object Models {
  val gridSize = 50
  val cellGrid = CellGrid(gridSize)
}
