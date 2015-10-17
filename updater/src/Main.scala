import collection.mutable._
import collection.JavaConversions._
import jdk.internal.org.objectweb.asm.tree._

object Main extends App {
	val cl94 = new Loader(94)
	val cl90 = new Loader(90)

	val node1 = cl94.classes("f")
	val node2 = cl90

	val results = MutableList[(String, Double)]()
	for(c <- cl90.classes.values)
		results += (c.name -> compare(c, node1, toMethodList))

	results.sortWith(_._2 > _._2).foreach(i => println(i._1 + ": " + i._2))

	def compare(one: ClassNode, two: ClassNode, func: (ClassNode) => List[String]) = {
		val count = new DynamicMap
		func(one).foreach(f => count.inc(f))
		func(two).foreach(f => count.dec(f))

		val total = if(count.incs > count.decs) count.incs else count.decs
		val amt = count.map.values.foldLeft(0)(Math.abs(_) + Math.abs(_))
		val percent = (total - amt * 0.5) / total
		percent
	}

	def fixObjNames(str: String) = str.replaceAll("L[a-z][a-z]?;", "O")
	def toMethodList(cl: ClassNode) =
		cl.methods.toList.filter(m => (m.access & 8) == 0).map(m => m.access + fixObjNames(m.desc))
	def toFieldList(cl: ClassNode) = cl.fields.toList.map(f => f.access + fixObjNames(f.desc))

	def all(loader: Loader) = {
		val result = Map[String, Map[String, Int]]()
		for(m <- loader.methods; i <- m._2.instructions.iterator;
		if i.getOpcode == 0xb4 || i.getOpcode == 0xb5){
			val field = i.asInstanceOf[FieldInsnNode]
			val name = field.owner + "." + field.name
			if(loader.fields.contains(name)){

				if(!result.contains(name))
					result += (name -> Map[String, Int]())
				if(!result(name).contains(m._1))
					result(name) += (m._1 -> 0)

				result(name)(m._1) += 1
			}
		}
		result
	}

}
