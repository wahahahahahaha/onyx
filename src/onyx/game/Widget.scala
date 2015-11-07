package onyx.game
import onyx.Reflect._

class Widgets {
}

class Widget(obj: Object) extends Reflected(obj){
	val index =			WIDGET_INDEX.getInt(obj)

	def parent =		new Widget(WIDGET_PARENT.get(obj))
	def x =				WIDGET_X.getInt(obj)
	def y =				WIDGET_Y.getInt(obj)
	def name =			WIDGET_NAME.get(obj).asInstanceOf[String]
	def text =			WIDGET_TEXT.get(obj).asInstanceOf[String]
	def hidden =		WIDGET_HIDDEN.get(obj).asInstanceOf[Boolean]
	def actions =		WIDGET_ACTIONS.get(obj).asInstanceOf[Array[String]]
}
