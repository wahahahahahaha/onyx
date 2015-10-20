import collection.mutable.LinkedHashMap
import collection.JavaConversions._
import java.lang.reflect.Modifier
import jdk.internal.org.objectweb.asm.tree._

class Updater(val rrev: Int, val trev: Int){
	val hooks_ref = LogParser(rrev)
	val hooks_tar = LinkedHashMap[String, String]()

	val reference = new Loader(rrev)
	val target = new Loader(trev)

	for(h <- hooks_ref; if !h._1.contains(".")){
		val best = findBestClass(h._1)
		val res = (best._1 -> h._2)
		hooks_tar += res
		printf("%s =>  %s (%.2f)\n", h._2.padTo(20, ' '), best._1.padTo(2, ' '), best._2)
	}

	def findBestClass(str: String) = {
		val item = reference.classes(str)
		val (fitem, mitem) = (fieldStrings(item), methodStrings(item))
		val sup = getDeobName(item.superName, 0)

		var (best, highest) = ("", 0.0)
		for(c <- target.classes.values){
			val s = if(sup == getDeobName(c.superName, 1)) 1 else 0
			val f = compare(fitem, fieldStrings(c))
			val m = compare(mitem, methodStrings(c))
			val avg = (f + m + s) / 3
			if(avg > highest){
				highest = avg
				best = c.name
			}
		}
		(best, highest)
	}

	def compare(one: List[String], two: List[String]) = {
		val count = new DynamicMap
		one.foreach(f => count inc fixDescriptor(f, 0))
		two.foreach(f => count dec fixDescriptor(f, 1))

		val total = if(count.incs > count.decs) count.incs else count.decs
		val amt = count.map.values.foldLeft(0)(Math.abs(_) + Math.abs(_))
		val percent = (total - amt * 0.5) / total
		percent
	}

	def getDeobName(str: String, which: Int) = {
		try {
			val name = (if(which == 0) hooks_ref else hooks_tar)(str)
			if((if(which == 0) hooks_tar else hooks_ref).values.contains(name))
				name
			else "O"
		} catch {
			case e: NoSuchElementException => "O"
		}
	}

	def fixDescriptor(str: String, which: Int) = {
		"L([a-z][a-z]?);".r.replaceAllIn(str, m => {
			val res = getDeobName(m.group(1), which)
			"|" + res + "|"
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
