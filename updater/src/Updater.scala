import collection.mutable.MutableList
import collection.JavaConversions._
import java.lang.reflect.Modifier
import jdk.internal.org.objectweb.asm.tree._

class Updater(val rrev: Int, val trev: Int){
	val hooks_ref = LogParser(rrev)
	val hooks_tar = MutableList[(String, String)]()

	val reference = new Loader(rrev)
	val target = new Loader(trev)

	for(h <- hooks_ref; if !h._1.contains(".")){
		val best = findBestClass(h._1)
		println(best._1 + " (" + best._2 + ")  =>  " + h._2)
	}

	def findBestClass(str: String) = {
		val item = reference.classes(str)
		val (fitem, mitem) = (fieldStrings(item), methodStrings(item))

		var (best, highest) = ("", 0.0)
		for(c <- target.classes.values){
			val f = compare(fitem, fieldStrings(c))
			val m = compare(mitem, methodStrings(c))
			val avg = (f + m) / 2
			if(avg > highest){
				highest = avg
				best = c.name
			}
		}
		(best, highest)
	}

	def compare(one: List[String], two: List[String]) = {
		val count = new DynamicMap
		one.foreach(f => count inc fixDescriptor(f))
		two.foreach(f => count dec fixDescriptor(f))

		val total = if(count.incs > count.decs) count.incs else count.decs
		val amt = count.map.values.foldLeft(0)(Math.abs(_) + Math.abs(_))
		val percent = (total - amt * 0.5) / total
		percent
	}

	def fixDescriptor(str: String) = {
		"L([a-z][a-z]?);".r.replaceAllIn(str, m => {
			"O"
		})
	}

	def methodStrings(cl: ClassNode) =
		cl.methods.toList.filter(m => !Modifier.isStatic(m.access)).map(m => m.access + m.desc)
	def fieldStrings(cl: ClassNode) =
		cl.fields.toList.filter(f => !Modifier.isStatic(f.access)).map(f => f.access + f.desc)
}

class DynamicMap {
	var incs, decs = 0
	val map = collection.mutable.Map[String, Int]()

	def inc(key: String){ incs += 1; change(key,  1) }
	def dec(key: String){ decs += 1; change(key, -1) }
	private def change(key: String, amt: Int){
		try
			map(key) += amt
		catch { case e: Exception =>
			map += (key -> amt) }
	}
}
