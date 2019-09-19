import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

trait HealthAdviceRepository {
  def all(): Future[Seq[HealthAdvice]]
  def getCategory(category: String): Future[Seq[HealthAdvice]]
  def getOneRandomAdvice(): Future[HealthAdvice]
}

class InMemoryHealthRepository(val healthAdvices: List[HealthAdvice]=Nil)(implicit ec:ExecutionContext)
  extends HealthAdviceRepository {

  override def all(): Future[Seq[HealthAdvice]] = Future.successful {
    try {
      this.healthAdvices.toVector
    } catch {
      case x: Throwable => println(x.getMessage)
        Nil
    }
  }

  override def getCategory(categoryAsked: String): Future[Seq[HealthAdvice]] = {
    Future.successful {
      try {
        healthAdvices.filter(x=>{
          x.category match {
            case Some(x) => x.name==categoryAsked
            case None => false
          }
        })
      } catch {
        case x: Exception => println("error" + x.getMessage)
          Nil.toVector
      }
    }
  }

  def getOneRandomAdvice={
    Future.successful{
      val r = new Random()
      val adviceNumber = (r.nextDouble() * this.healthAdvices.size).toInt
      healthAdvices(adviceNumber)
    }
  }
}