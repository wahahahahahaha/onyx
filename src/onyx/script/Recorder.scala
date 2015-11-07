package onyx.script

import java.io.{ BufferedWriter, OutputStreamWriter, FileOutputStream }
import java.awt.event.{ MouseEvent, MouseListener, MouseMotionListener }

class Recorder extends MouseListener with MouseMotionListener {
	val out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				"record/" + System.currentTimeMillis)))

	def mouseEntered(m: MouseEvent){
		record(m)
	}
	def mouseExited(m: MouseEvent){
		record(m)
	}
	def mouseClicked(m: MouseEvent){
		record(m)
	}
	def mousePressed(m: MouseEvent){
		record(m)
	}
	def mouseReleased(m: MouseEvent){
		record(m)
	}
	def mouseDragged(m: MouseEvent){
		record(m)
	}
	def mouseMoved(m: MouseEvent){
		record(m)
	}

	private def record(m: MouseEvent){

	}
}
