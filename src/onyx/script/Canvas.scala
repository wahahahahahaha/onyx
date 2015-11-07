package onyx.script

import collection.mutable.ListBuffer
import java.awt.Color
import java.awt.AWTEvent
import java.awt.image.BufferedImage
import java.util.EventListener
import java.awt.event.{ MouseListener, MouseMotionListener }

class Canvas(val old: java.awt.Canvas) extends java.awt.Canvas {
	protected[script] val points = ListBuffer[MouseBit]()
	protected[script] var input = true

	private val img = new BufferedImage(old.getWidth, old.getHeight, BufferedImage.TYPE_INT_RGB)

	override def getGraphics() = {
		val g = img.getGraphics
		if(!input){
			g.setColor(new Color(255, 0, 0, 50))
			g.fillRect(0, 0, img.getWidth, img.getHeight)
		}
		for(m <- points.toList){
			g.setColor(m.color)
			val size = if(m.color != Color.WHITE) 2 else 1
			g.fillRect(m.point.getX.toInt, m.point.getY.toInt, size, size)
			if(System.currentTimeMillis - m.time > 500){
				points -= m
			}
		}
		super.getGraphics().drawImage(img, 0, 0, null); g
	}
	override def hashCode = old.hashCode
}
