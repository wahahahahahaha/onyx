package onyx.game

class Reflected(private val obj: Object){

	override def equals(o: Any) =
		obj.equals(o)
}
