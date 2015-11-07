package onyx.game
import onyx.Reflect._

class Client {
	def width =			CLIENT_VIEWPORTWIDTH.getInt
	def height =		CLIENT_VIEWPORTHEIGHT.getInt
	def scale =			CLIENT_VIEWPORTSCALE.getInt

	def state =			CLIENT_GAMESTATE.getInt
}
