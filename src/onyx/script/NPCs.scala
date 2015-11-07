package onyx.script

import onyx.game.NPC
import onyx.Reflect._

class NPCs extends CharacterFilter[NPC] {

	def load = { update(CLIENT_LOADEDNPCS, new NPC(_)); this }
}
