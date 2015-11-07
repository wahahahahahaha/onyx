package onyx.updater

import onyx.Hook
import scala.io.Source
import java.net.URLClassLoader

object Updater {

	/* Thanks Dex, slimy leecher here. Just temporary, promise. */
	def apply(rev: Int, loader: URLClassLoader) = {
		val lines = Source.fromFile("logs/" + rev + ".txt").getLines
		val nfield = """.*?Field.*?(\w+\.\w+)\(.*?\)\s*identified as ([\-A-Za-z]+)""".r
		val nmult = """.*?Field.*?(\w+\.\w+)\(.*?\) \* (-?\d+)\s*identified as ([\-A-Za-z]+)""".r
		lines.map(_ match {
			case nmult(id, mult, name) =>	hook(loader, id, name, mult)
			case nfield(id, name) =>		hook(loader, id, name)
			case default => null
		}).filter(_ != null).toList
	}

	private def hook(loader: URLClassLoader, id: String, name: String, mult: String = "1") = {
		val full = name.replaceAll("-", "_").toUpperCase
		val split = id.split('.')
		val field = loader.loadClass(split(0)).getDeclaredField(split(1))
		field.setAccessible(true)
		new Hook(full, id, field, mult.toInt)
	}
}
