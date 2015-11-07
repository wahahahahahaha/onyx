package onyx.game
import onyx.Reflect._

class Entity(obj: Object) extends Reflected(obj){
	val id =			ENTITY_ID.getInt(obj)

	def worldx =		ENTITY_WORLDX.getInt(obj)
	def worldy =		ENTITY_WORLDY.getInt(obj)
	def offx =			ENTITY_OFFSETX.getInt(obj)
	def offy =			ENTITY_OFFSETY.getInt(obj)
	def x =				ENTITY_X.getInt(obj)
	def y =				ENTITY_Y.getInt(obj)
	def plane =			ENTITY_PLANE.getInt(obj)
}
