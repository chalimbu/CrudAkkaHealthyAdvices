import scala.util.{MurmurHash, Random}

case class HealthAdvice(val advice:String,val category: Option[Category]) {
  override def toString: String = {
    advice + {
      this.category match {
        case Some(cat) => s",${cat}"
        case None => ""
      }
    }//.
  }
}

object HealthAdvice{
  def generateSomeHealtAvices: List[HealthAdvice]= {
    val category=new Category("Exercise")
    val category2=new Category("Alimentation")
    val l=List(HealthAdvice("Exercise regurlaly",Some(category)),
      HealthAdvice("Make 1 hour of exercise by day",Some(category)),
      HealthAdvice("Eat a fruit everyday",Some(category2)),
      HealthAdvice("Eat fruits and vegetables",Some(category2)),
      HealthAdvice("Eat fruits and vegetables",None))

    l
  }
  def getOneRandomAdvice(l:List[HealthAdvice]): HealthAdvice={
    val r=new Random()
    val adviceNumber=(r.nextDouble()*l.size).toInt
    l(adviceNumber)
  }
}
