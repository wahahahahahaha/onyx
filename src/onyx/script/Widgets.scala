package onyx.script

import onyx.game.Widget
import onyx.Reflect._

class Widgets extends Filter[List[Widget]] {

	def load = { update(CLIENT_WIDGETS, a => {
		a.asInstanceOf[Array[Object]].map(new Widget(_)).toList
	}); this }
}
