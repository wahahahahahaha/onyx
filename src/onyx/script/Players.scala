package onyx.script

import onyx.game.Player
import onyx.Reflect._

class Players extends CharacterFilter[Player] {

	def load = { update(CLIENT_LOADEDPLAYERS, new Player(_)); this }
}
