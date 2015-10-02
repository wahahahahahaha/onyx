package onyx
import java.lang.reflect.Field
import java.net.URLClassLoader

class Hook(val clazz: String, val field: String, val mult: Int = 1)

class Reflection(val loader: URLClassLoader) {
	private var cache = Map[Hook, Field]()

	def getField(h: Hook) = {
		try {
			cache(h)
		} catch { case e: Exception => {
			val field = loader.loadClass(h.clazz).getDeclaredField(h.field)
			field.setAccessible(true)
			cache = cache + (h -> field)
			field
		}}
	}

	def getValue(h: Hook, instance: Any = null) =
		getField(h).get(instance)

	def getInteger(h: Hook, instance: Any = null) =
		getValue(h, instance).asInstanceOf[Int] * h.mult
}
