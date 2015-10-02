package onyx
import javax.swing.JFrame;
import javax.swing.WindowConstants;

object Onyx extends App {
	val frame = new JFrame
	var client: Client = _

	reload()
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
	frame.setLocation(25, 25)
	frame.setResizable(false)
	frame.setVisible(true)

	def reload(){
		client = new Client
		frame.getContentPane().removeAll()
		frame.add(client.applet)
		resize(815, 736)
	}
	def resize(w: Int, h: Int){
		val dim = new java.awt.Dimension(w, h)
		frame.getContentPane().setPreferredSize(dim)
		client.applet.setSize(dim)
		frame.pack()
	}
}
