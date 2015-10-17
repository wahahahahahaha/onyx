class DynamicMap {
	var incs = 0
	var decs = 0
	val map = collection.mutable.Map[String, Int]()

	def change(key: String, amt: Int){
		try {
			map(key) += amt
		} catch {
			case e: Exception => map += (key -> amt)
		}
	}

	def inc(key: String) = {
		incs += 1
		change(key, 1)
	}
	def dec(key: String) = {
		decs += 1
		change(key, -1)
	}

}
