package onyx.script

import onyx.Hook
import onyx.Reflect._
import onyx.game.Character

class Filter[T] {
	/*protected[script]*/ var data: List[T] = null

	def update(h: Hook, f: Object => T) =
		data = h.get.asInstanceOf[Array[Object]].filter(_ != null).map(f).toList

	def first = data = List( data(0) )
	def get(i: Int) = data(i)
	def get = data(0)

	def foreach(f: T => Any) = data.foreach(f)
}

class CharacterFilter[T <: Character] extends Filter[T] {

	def animation(a: Int): this.type =
		{ data = data.filter(c => c.animation == a); this }
}
