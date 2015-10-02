package onyx
import scala.language.postfixOps
import sys.process._
import java.io.File
import java.net.URL
import java.nio.file.{Paths, Files}

object Util {
	def getSource(url: String) =
		io.Source.fromURL(url).mkString

	def download(url: String, force: Boolean = false){
		val name = url.substring(url.lastIndexOf('/') + 1)
		if(force || !Files.exists(Paths.get(name))){
			print(s"Downloading $name ... ")
			new URL(url) #> new File(name) !!;
			println("Finished!")
		}
	}
}
