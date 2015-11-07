package onyx.script

class Methods {
	private val rng = new MersenneTwister(System.currentTimeMillis.toInt)

	def rand(min: Int, max: Int) = { min + rng.nextInt(max - min + 1) }

	def sleep(min: Int, max: Int){ sleep(rand(min, max)) }
	def sleep(ms: Int){ sleepNano(ms * 1000000) }

	def sleepNano(min: Int, max: Int){ sleepNano(rand(min, max)) }
	def sleepNano(ns: Long){
		val start = System.nanoTime
		while(System.nanoTime - start < ns){}
	}
}
