package onyx

import onyx.updater.Updater
import onyx.script.Context
import scala.language.postfixOps
import scala.sys.process._
import java.applet.{ Applet, AppletStub }
import java.net.{ URL, URLClassLoader }
import java.nio.file.{ Paths, Files }
import java.awt.Canvas
import java.io.File

class Client(force: Boolean = false) {
	val url = "http://oldschool30.runescape.com"
	download(url + "/gamepack.jar")

	val loader = new URLClassLoader(Array(new URL("file:gamepack.jar")))
	val hooks = Updater(98, loader)
	Reflect.init(hooks)

	val applet = loader.loadClass("client").newInstance().asInstanceOf[Applet]

	new Thread(new Runnable { def run {
		while(Reflect.CLIENT_GAMESTATE.getInt != 5){
			Thread sleep 5
		}
		val ctx = new Context(applet)
		val script = new Script(ctx)
		ctx.canvas.addMouseListener(script)

		while(true){ script.loop() }
	}}).start()

	applet.setStub(new Stub(url, getSource(url)))
	applet.init()
	applet.start()

	private def getSource(url: String) = io.Source.fromURL(url).mkString
	private def download(url: String){
		val name = url.substring(url.lastIndexOf('/') + 1)
		if(force || !Files.exists(Paths.get(name))){
			print(s"Downloading $name ... ")
			new URL(url) #> new File(name) !!;
			println("Finished!")
		}
	}
}

class Stub(url: String, src: String) extends AppletStub {
	val base = new URL(url + "/gamepack.jar")
	val params= """<param name="([^"]*)" value="([^"]*)">""".r
		.findAllIn(src).matchData.map(n => n.group(1) -> n.group(2)).toMap

	def getCodeBase() = base
	def getDocumentBase() = base
	def getParameter(s: String) = params(s)

	def getAppletContext() = null
	def isActive() = false
	def appletResize(x: Int, y: Int) = {}
}
