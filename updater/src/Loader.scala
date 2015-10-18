import collection.JavaConversions._
import util.control.Exception
import java.util.jar.JarFile
import java.lang.StringBuilder
import jdk.internal.org.objectweb.asm.ClassReader
import jdk.internal.org.objectweb.asm.tree._

class Loader(val rev: Int){
	val classes = loadClasses()
	val methods = loadMethods()
	val fields = loadFields()

	private def findMethod(full: String) = {
		val Array(owner, name, desc) = full.split(" ")
		classes(owner).methods.find(m => m.name == name && m.desc == desc)
	}

	private def findSupers(clname: String) = {
		val result = collection.mutable.Set[ClassNode]()
		Exception.ignoring(classOf[NoSuchElementException]) {
			var target = classes(clname)
			while(!target.name.contains("java")){
				result += target
				target = classes(target.superName)
			}
		}
		result.toSet
	}

	private def findSubs(clname: String): Set[ClassNode] = {
		try {
			findSubs(classes(clname))
		} catch {
			case e: NoSuchElementException => Set()
		}
	}

	private def findSubs(clazz: ClassNode): Set[ClassNode] = {
		val result = collection.mutable.Set[ClassNode]( clazz )
		for(c <- classes.values; if c.superName == clazz.name)
			result ++= findSubs( c )
		result.toSet
	}

	private def findRelated(clname: String) = {
		findSubs(clname) ++ findSupers(clname)
	}

	private def findMethods(full: String) = {
		val Array(owner, name, desc) = full.split(" ")
		val related = findRelated(owner)
		val result = collection.mutable.Map[String, MethodNode]()

		for(c <- related){
			val temp = c.name + " " + name + " " + desc
			val method = findMethod(temp)
			if(method != None) result += (temp -> method.get)
		}
		result.toMap
	}

	private def addMethods(methods: collection.mutable.Map[String, MethodNode], full: String){
		val found = findMethods(full)
		methods ++= found

		for(m <- found; i <- m._2.instructions.iterator; if isInvoke(i.getOpcode)){
			val insn = i.asInstanceOf[MethodInsnNode]
			val desc = insn.owner + " " + insn.name + " " + insn.desc
			if(!methods.contains(desc))
				addMethods(methods, desc)
		}
	}

	private def hasFakeParam(method: MethodNode): Boolean = {
		val arr = method.instructions.toArray
		for(i <- 3 until arr.length; if arr(i).getOpcode == 0xbb){
			val tdesc = arr(i).asInstanceOf[TypeInsnNode].desc
			if(tdesc.contains("IllegalState") && arr(i - 3).getOpcode == 0x15)
				return true
		}
		false
	}

	private def removeFakeParams(methods: Iterable[MethodNode]){
		for(m <- methods; if hasFakeParam(m)){
			m.desc = new StringBuilder(m.desc).deleteCharAt(m.desc.indexOf(')') - 1).toString
		}
	}

	private def loadFields() = {
		val fields = collection.mutable.Map[String, FieldNode]()
		for(m <- methods.values; i <- m.instructions.iterator; if isField(i.getOpcode)){
			val insn = i.asInstanceOf[FieldInsnNode]
			val supers = findSupers(insn.owner)
			for(c <- supers){
				Exception.ignoring(classOf[NoSuchElementException]){
					val field = c.fields.find(_.name == insn.name).get
					fields += (c.name + "." + insn.name -> field)
				}
			}
		}
		for(c <- classes.values; f <- c.fields.toList; if !fields.values.contains(f))
			c.fields.remove(f)

		log("Kept " + fields.size + " fields.")
		fields.toMap
	}

	private def loadMethods() = {
		val methods = collection.mutable.Map[String, MethodNode]()
		for(c <- classes.values; m <- c.methods; if m.name.length > 2; if m.name != "<init>")
			addMethods(methods, c.name + " " + m.name + " " + m.desc)
		for(c <- classes.values; m <- c.methods.toList; if !methods.values.contains(m))
			c.methods.remove(m)

		removeFakeParams(methods.values)

		log("Kept " + methods.size + " methods.")
		methods.toMap
	}

	private def loadClasses() = {
		var classes = collection.mutable.Map[String, ClassNode]()
		val jar = new JarFile(rev + ".jar")
		for(e <- jar.entries; if e.getName.endsWith(".class")){
			val reader = new ClassReader(jar.getInputStream(e))
			val node = new ClassNode()
			reader.accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES)
			classes += (node.name -> node)
		}
		jar.close()
		classes.toMap
	}

	def log(str: String) = println("(" + rev + ") " + str)
	def isField(code: Int) = (code >= 0xb2 && code <= 0xb5)
	def isInvoke(code: Int) = (code >= 0xb6 && code <= 0xba)
}
