package onyx.game
import onyx.Reflect._

class Player(obj: Object) extends Character(obj){
	val name =			PLAYER_NAME.get(obj).asInstanceOf[String]

	def level =			PLAYER_COMBATLEVEL.getInt(obj)
}
