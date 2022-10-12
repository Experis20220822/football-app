package models

case class Stadium(id:Long, name: String, city: String, country:String, seats:Int ) {

}

object Stadium {
  def apply(name: String) = new Stadium(0, name, "London", "UK", 1000)
}

