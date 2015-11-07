package onyx.script

import java.awt.event.{ KeyEvent, KeyListener }

class Keyboard(val canvas: Canvas) {
	private val key = new KeyListener {
		def keyPressed(k: KeyEvent){ dispatch(k) }
		def keyReleased(k: KeyEvent){ dispatch(k) }
		def keyTyped(k: KeyEvent){ dispatch(k) }
	}
	canvas.addKeyListener(key)

	private def dispatch(k: KeyEvent){
		canvas.old.dispatchEvent(k)
	}
}
