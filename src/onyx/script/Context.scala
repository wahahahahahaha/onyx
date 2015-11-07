package onyx.script

import onyx.Reflect
import onyx.game.Player
import java.awt.Point
import java.awt.Color
import java.applet.Applet

case class MouseBit(
	point: Point,
	color: Color,
	time: Long
)

class Context(applet: Applet){
	val players = new Players
	val npcs = new NPCs
	val widgets = new Widgets

	val canvas = {
		val old = Reflect.CLIENT_CANVAS.get.asInstanceOf[java.awt.Canvas]
		val canvas = new Canvas(old)
		Reflect.CLIENT_CANVAS.set(canvas)
		applet.add(canvas)
		applet.remove(old)
		canvas
	}
	val mouse = new Mouse(canvas)
	val keys = new Keyboard(canvas)
}
