package onyx

import onyx.updater.Updater
import java.awt.Canvas
import java.awt.event.MouseListener
import java.applet.Applet
import java.applet.AppletStub
import java.net.URLClassLoader
import java.net.URL

class Client(force: Boolean = false) {
	val url = "http://oldschool30.runescape.com"
	Util.download(url + "/gamepack.jar", force)

	val loader = new URLClassLoader(Array(new URL("file:gamepack.jar")))
	val hooks = Updater(loader)
	Reflect.init(hooks)

	val applet = loader.loadClass("client").newInstance().asInstanceOf[Applet]
	applet.setStub(new Stub(url, Util.getSource(url)))
	applet.init()
	applet.start()
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
