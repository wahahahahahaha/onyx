package onyx
import java.lang.reflect.Field
import java.net.URLClassLoader

class Hook(
	val name: String,
	val id: String,
	val field: Field,
	val mult: Int		){

	def get(obj: Object = null) =
		field.get(obj)
	def getInt(obj: Object = null) =
		field.get(obj).asInstanceOf[Int] * mult

	override def toString = if(mult == 1) id else (id + " * " + mult)
}

object Reflect {

	/*val HOOK_NAME: Hook = null*/

	def init(hooks: List[Hook]) = {
		val fields = this.getClass.getDeclaredFields

		for(h <- hooks){
			val f = fields.find(f => f.getName == h.name).get
			f.setAccessible(true)
			f.set(this, h)
			println(h.name.padTo(26, ' ') + h)
		}
	}
}

