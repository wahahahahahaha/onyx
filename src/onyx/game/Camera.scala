package onyx.game
import onyx.Reflect._

class Camera {
	def pitch =		CLIENT_CAMERAPITCH.getInt
	def yaw =		CLIENT_CAMERAYAW.getInt
	def x =			CLIENT_CAMERAX.getInt
	def y =			CLIENT_CAMERAY.getInt
	def z =			CLIENT_CAMERAZ.getInt
}
