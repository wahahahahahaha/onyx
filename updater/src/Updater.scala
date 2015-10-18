import collection.JavaConversions._
import java.lang.reflect.Modifier
import jdk.internal.org.objectweb.asm.tree._

class Updater(val rrev: Int, val trev: Int){
	val reference = new Loader(rrev, LogParser(rrev))
	val target = new Loader(trev)

	for(h <- reference.hooks; if !h._1.contains(".")){
		val best = findBestMatch(h._1)
		println(best._1 + " (" + best._2 + ")  =>  " + h._2)
	}

	def findBestMatch(str: String) = {
		if(str.contains(".")){
			("", 0)
		} else {
			val item = reference.classes(str)
			var (best, highest) = ("", 0.0)
			for(c <- target.classes.values){
				val f = compare(item, c, toFieldList)
				val m = compare(item, c, toMethodList)
				val avg = (f + m) / 2
				if(avg > highest){
					highest = avg
					best = c.name
				}
			}
			(best, highest)
		}
	}

	def compare(one: ClassNode, two: ClassNode, func: (ClassNode) => List[String]) = {
		val count = new DynamicMap
		func(one).foreach(f => count inc fixDescriptor(f))
		func(two).foreach(f => count dec fixDescriptor(f))

		val total = if(count.incs > count.decs) count.incs else count.decs
		val amt = count.map.values.foldLeft(0)(Math.abs(_) + Math.abs(_))
		val percent = (total - amt * 0.5) / total
		percent
	}

	def fixDescriptor(str: String) = {
		/* temp */
		"L([a-z][a-z]?);".r.replaceAllIn(str, m => {
			"O"
		})
	}

	def toMethodList(cl: ClassNode) =
		cl.methods.toList.filter(m => !Modifier.isStatic(m.access)).map(m => m.access + m.desc)
	def toFieldList(cl: ClassNode) =
		cl.fields.toList.filter(f => !Modifier.isStatic(f.access)).map(f => f.access + f.desc)

}
