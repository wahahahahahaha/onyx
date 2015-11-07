package onyx.game
import onyx.Reflect._

class Character(obj: Object) extends Renderable(obj){
	val maxhealth =		CHARACTER_MAXHEALTH.getInt(obj)

	def orientation =	CHARACTER_ORIENTATION.getInt(obj)
	def animation =		CHARACTER_ANIMATION.getInt(obj)
	def health =		CHARACTER_HEALTH.getInt(obj)
	def target =		CHARACTER_INTERACTIONINDEX.getInt(obj)
	def x =				CHARACTER_X.getInt(obj)
	def y =				CHARACTER_Y.getInt(obj)
}
