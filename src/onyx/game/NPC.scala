package onyx.game
import onyx.Reflect._

class NPC(obj: Object) extends Character(obj){
	private val definition = NPC_DEFINITION.get(obj)

	val actions =		NPCDEF_ACTIONS.get(definition).asInstanceOf[Array[String]]
	val level =			NPCDEF_COMBATLEVEL.getInt(definition)
	val name =			NPCDEF_NAME.get(definition).asInstanceOf[String]
	val id =			NPCDEF_ID.getInt(definition)
}
