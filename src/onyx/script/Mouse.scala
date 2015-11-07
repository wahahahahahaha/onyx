package onyx.script

import java.awt.AWTEvent
import java.awt.Color
import java.awt.event.{ MouseListener, MouseMotionListener, FocusListener }
import java.awt.event.{ MouseEvent, FocusEvent }

class Mouse(val canvas: Canvas){
	protected[script] var disabled = false
	private var pressed = false
	private var x, y = 0

	private val mouse = new MouseListener {
		def mouseReleased(m: MouseEvent){ dispatch(m) }
		def mouseEntered(m: MouseEvent){ dispatch(m) }
		def mouseClicked(m: MouseEvent){ dispatch(m) }
		def mousePressed(m: MouseEvent){ dispatch(m) }
		def mouseExited(m: MouseEvent){ dispatch(m) }
	}
	private val mmouse = new MouseMotionListener {
		def mouseDragged(m: MouseEvent){ dispatch(m) }
		def mouseMoved(m: MouseEvent){ dispatch(m) }
	}
	canvas.addMouseListener(mouse)
	canvas.addMouseMotionListener(mmouse)

	def down(){
		mouse.mousePressed(event(501, 1024, 1))
	}
	def up(){
		mouse.mouseReleased(event(502, 0, 1))
		/*if(xdown == x && ydown == y) mouse.mouseClicked(event(500, 16, 1))*/
	}
	def disable(){ disabled = true; canvas.input = false }
	def enable(){ disabled = false; canvas.input = true }

	private def dispatch(m: MouseEvent){
		if(disabled && m.getClickCount != 100)
			return

		x = m.getX
		y = m.getY
		try {
			canvas.points += MouseBit(
				m.getPoint,
				m.getID match {
					case 501 => Color.GREEN
					case 502 => Color.RED
					case 503 => Color.WHITE
					case 506 => Color.YELLOW
				}, System.currentTimeMillis
			)
		} catch { case e: MatchError => {} }

		canvas.old.dispatchEvent(m)
	}
	private def event(id: Int, mod: Int, btn: Int) = {
		val off = canvas.getLocationOnScreen
		new MouseEvent(canvas, id, System.currentTimeMillis,
			mod, x, y, x + off.getX.toInt, y + off.getY.toInt,
			100, false, btn);
	}
}
