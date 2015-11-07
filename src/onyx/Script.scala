package onyx

import java.awt.event.{ MouseEvent, MouseListener }
import onyx.script.Context
import onyx.script.Methods

class Script(ctx: Context) extends Methods with MouseListener {

	def loop(){

		sleep(25)
	}

	def mouseEntered(m: MouseEvent){}
	def mouseExited(m: MouseEvent){}
	def mouseReleased(m: MouseEvent){}
	def mousePressed(m: MouseEvent){}
	def mouseClicked(m: MouseEvent){}
}
